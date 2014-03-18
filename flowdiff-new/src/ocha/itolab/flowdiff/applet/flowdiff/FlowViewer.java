package ocha.itolab.flowdiff.applet.flowdiff;

import java.awt.*;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;

public class FlowViewer extends JApplet {

	// GUI element
	MenuBar menuBar;
	ViewingPanel viewingPanel = null; 
	CursorListener cl;
	Canvas canvas;
	Container windowContainer;
	
	
	/**
	 * applet を初期化し、各種データ構造を初期化する
	 */
	public void init() {
		buildGUI();
	}

	/**
	 * applet の各イベントの受付をスタートする
	 */
	public void start() {
		setSize(new Dimension(1000,800));
	}

	/**
	 * applet の各イベントの受付をストップする
	 */
	public void stop() {
	}

	/**
	 * applet等を初期化する
	 */
	private void buildGUI() {

		
		// Canvas
		canvas = new Canvas(512, 512);
		canvas.requestFocus();
		GLCanvas glc = canvas.getGLCanvas();
		
		// ViewingPanel
		viewingPanel = new ViewingPanel();
		viewingPanel.setCanvas(canvas);
		
		// MenuBar
		menuBar = new MenuBar();
		menuBar.setCanvas(canvas);
		
		// CursorListener
		cl = new CursorListener();
		cl.setCanvas(canvas, glc);
		cl.setViewingPanel(viewingPanel);
		canvas.addCursorListener(cl);
		
		// CanvasとViewingPanelのレイアウト
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(glc, BorderLayout.CENTER);
		mainPanel.add(viewingPanel, BorderLayout.WEST);

		// ウィンドウ上のレイアウト
		windowContainer = this.getContentPane();
		windowContainer.setLayout(new BorderLayout());
		windowContainer.add(mainPanel, BorderLayout.CENTER);
		windowContainer.add(menuBar, BorderLayout.NORTH);
		
	}

	/**
	 * main関数
	 * @param args 実行時の引数
	 */
	public static void main(String[] args) {
		Window window = new Window(
				"Flow Viewer", 800, 600, Color.LIGHT_GRAY);
		FlowViewer fv = new FlowViewer();

		fv.init();
		window.getContentPane().add(fv);
		window.setVisible(true);

		fv.start(); 
	}
		
}

