
package ocha.itolab.flowdiff.applet.flowdiff;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ocha.itolab.flowdiff.core.data.*;
import ocha.itolab.flowdiff.core.streamline.*;


public class ViewingPanel extends JPanel {

	// 流れ場のファイルを読み込む（相対パス）
	static String url1 = "file:../data/kassoro/ari/";
	static String url2 = "file:../data/kassoro/nashi/";  	
	
	/* パネルのボタン・ラジオボタン・スライダ類 */
	public JButton  openDataButton, viewResetButton, viewBuildingButton,generateStreamlineButton, viewVectorButton,
					resetAllStreamlineButton,removeStreamlineButton,highlightStreamline;
	public JRadioButton viewRotateButton, viewScaleButton, viewShiftButton, noneGridView, grid1View, grid2View, bothGridView,
						noneRotView, grid1RotView, grid2RotView, bothRotView,viewRotate0,viewRotate1,viewRotate2,viewRotate3,viewRotate4,viewRotate5,
						showDiffAngView,showDiffLenView,noneDiffView,showDiffVectorView,showDiffVectorViewLength;
	public JLabel xText, yText, zText, vtext, vhText, vecviewText, diffText;
	public JSlider sliderX, sliderY, sliderZ,sliderVH,vheight,sliderDiff;
	public JList list;
	public DefaultListModel model;
	public Container container;
	File currentDirectory;

	/* Selective canvas */
	Canvas canvas;

	/* Cursor Sensor */
	boolean cursorSensorFlag = false;

	/* Action listener */
	ButtonListener bl = null;
	RadioButtonListener rbl = null;
	CheckBoxListener cbl = null;
	SliderListener sl = null;

	/* Data */
	Grid grid1 = null;
	Grid grid2 = null;
	
	public ViewingPanel() {
		// super class init
		super();
		setSize(200, 800);

		JTabbedPane tabbedpane = new JTabbedPane();
		
		/* タブ1　ファイル読込・表示について*/
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(14,1));
		openDataButton = new JButton("ファイル読込");
		viewResetButton = new JButton("元に戻す");
		p1.add(openDataButton);
		p1.add(viewResetButton);
		p1.add(new JLabel("操作"));
		//ドラッグした際のマウスの動き指定
		ButtonGroup group1 = new ButtonGroup();
		viewRotateButton = new JRadioButton("回転",true);//最初にチェックが入っている
		group1.add(viewRotateButton);
		p1.add(viewRotateButton);
		viewScaleButton = new JRadioButton("拡大・縮小");
		group1.add(viewScaleButton);
		p1.add(viewScaleButton);
		viewShiftButton = new JRadioButton("移動");
		group1.add(viewShiftButton);
		p1.add(viewShiftButton);
		//視点切り替えラジオボタン
		p1.add(new JLabel("視点切り替え"));
		ButtonGroup group4 = new ButtonGroup();
		viewRotate0 = new JRadioButton("斜め", true);//最初にチェックが入っている
		group4.add(viewRotate0);
		p1.add(viewRotate0);
		viewRotate1 = new JRadioButton("真上",true);
		group4.add(viewRotate1);
		p1.add(viewRotate1);
		viewRotate2 = new JRadioButton("正面");
		group4.add(viewRotate2);
		p1.add(viewRotate2);
		viewRotate3 = new JRadioButton("後ろ");
		group4.add(viewRotate3);
		p1.add(viewRotate3);
		viewRotate4 = new JRadioButton("右横");
		group4.add(viewRotate4);
		p1.add(viewRotate4);
		viewRotate5 = new JRadioButton("左横");
		group4.add(viewRotate5);
		p1.add(viewRotate5);
		//建物を表示するかどうかのボタン設置
		viewBuildingButton = new JButton("建物表示");
		p1.add(viewBuildingButton);

		/*タブ2　ベクトルの表示*/
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(7,1));
		//ベクトル表示に関するラジオボタン
		vecviewText = new JLabel("ベクトル表示");
		p2.add(vecviewText);
		ButtonGroup group2 = new ButtonGroup();
		noneGridView = new JRadioButton("なし",true);//最初にチェックが入っている
		group2.add(noneGridView);
		p2.add(noneGridView);
		bothGridView = new JRadioButton("両方");
		group2.add(bothGridView);
		p2.add(bothGridView);
		grid1View = new JRadioButton("建物有(ベクトル白)");
		group2.add(grid1View);
		p2.add(grid1View);
		grid2View = new JRadioButton("建物無(ベクトル赤)");
		group2.add(grid2View);
		p2.add(grid2View);
		//ベクトル面の高さを設定するスライダ
		vheight = new JSlider(0, 85, 10);//TODO: 数字を変数にする
		vtext = new JLabel(" ベクトル面地上から: " + vheight.getValue());
		vheight.setMajorTickSpacing(10);
		vheight.setMinorTickSpacing(5);
		vheight.setPaintTicks(true);
		vheight.setLabelTable(vheight.createStandardLabels(20));
		vheight.setPaintLabels(true);
		p2.add(vheight);
		p2.add(vtext);

		/*タブ3 渦度に関する表示*/
		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(6,1));
		//渦度を表示するどうかのラジオボタン
		ButtonGroup group3 = new ButtonGroup();
		noneRotView = new JRadioButton("なし", true);//最初にチェックが入っている
		group3.add(noneRotView);
		p3.add(noneRotView);
		grid1RotView = new JRadioButton("両方");//最初にチェックが入っている
		group3.add(grid1RotView);
		p3.add(grid1RotView);
		grid2RotView = new JRadioButton("建物有(ベクトル白)");
		group3.add(grid2RotView);
		p3.add(grid2RotView);
		bothRotView = new JRadioButton("建物無(ベクトル赤)");
		group3.add(bothRotView);
		p3.add(bothRotView);
		//渦度の高さを変更する
		sliderVH = new JSlider(0, 85, 10);//TODO:スライダの数字を変数にする
		vhText = new JLabel(" 高さ(渦度): " + sliderVH.getValue());
		sliderVH.setMajorTickSpacing(10);
		sliderVH.setMinorTickSpacing(5);
		sliderVH.setPaintTicks(true);
		sliderVH.setLabelTable(sliderVH.createStandardLabels(20));
		sliderVH.setPaintLabels(true);
		p3.add(sliderVH);
		p3.add(vhText);
		

		/*タブ4　流線を生成するタブ*/
		JPanel p4 = new JPanel();
		p4.setLayout(new GridLayout(10,1));
		p4.add(new JLabel("流線表示"));
		p4.add(new JLabel("ピンク：建物有(ベクトル白)"));
		p4.add(new JLabel("水色：建物無(ベクトル赤)"));
		//流線を発生させるときの位置を決めるスライダ
		sliderX = new JSlider(0, 100, 10);
		sliderX.setMajorTickSpacing(10);
		sliderX.setMinorTickSpacing(5);
		sliderX.setPaintTicks(true);
		sliderX.setLabelTable(sliderX.createStandardLabels(20));
	    sliderX.setPaintLabels(true);
	    xText = new JLabel(" よこ: " + sliderX.getValue());
		p4.add(sliderX);
		p4.add(xText);
		sliderY = new JSlider(0, 100, 10);
		sliderY.setMajorTickSpacing(10);
		sliderY.setMinorTickSpacing(5);
		sliderY.setPaintTicks(true);
		sliderY.setLabelTable(sliderY.createStandardLabels(20));
	    sliderY.setPaintLabels(true);
	    yText = new JLabel(" 高さ: " + sliderY.getValue());
		p4.add(sliderY);
		p4.add(yText);
		sliderZ = new JSlider(0, 100, 10);
		sliderZ.setMajorTickSpacing(10);
		sliderZ.setMinorTickSpacing(5);
		sliderZ.setPaintTicks(true);
		sliderZ.setLabelTable(sliderZ.createStandardLabels(20));
	    sliderZ.setPaintLabels(true);
	    zText = new JLabel(" たて: " + sliderZ.getValue());
		p4.add(sliderZ);
		p4.add(zText);
		//流線を発生させるボタン
		generateStreamlineButton = new JButton("流線決定");
		p4.add(generateStreamlineButton);

		
		/*タブ5 生成した流線の一覧表示・削除など*/
		JPanel p5 = new JPanel();
		p5.setLayout(new GridLayout(2,1));
	    model = new DefaultListModel();
	    list = new JList(model);
	    //流線座標一覧表示を行う箇所
	    JScrollPane sp = new JScrollPane();
	    sp.getViewport().setView(list);
	    sp.setPreferredSize(new Dimension(200, 100));
	    //ハイライト・削除ボタンの表示
	   // JLabel label = new JLabel();
	    highlightStreamline = new JButton("ハイライト");
	    removeStreamlineButton = new JButton("削除");
	    resetAllStreamlineButton = new JButton("全て削除");
	    JPanel pb = new JPanel();
	    //pb.add(label);
	    pb.add(highlightStreamline);
	    pb.add(removeStreamlineButton);
		pb.add(resetAllStreamlineButton);
	    p5.add(sp);
	    p5.add(pb);
	    
	    
		/*タブ6 差分に関するもの*/
		JPanel p6 = new JPanel();
		p6.setLayout(new GridLayout(8,1));
		p6.add(new JLabel("差分表示"));
		//差分表示に関するラジオボタン
		ButtonGroup group5 = new ButtonGroup();
		noneDiffView = new JRadioButton("表示しない", true);//最初にチェックが入っている
		group5.add(noneDiffView);
		p6.add(noneDiffView);
		showDiffAngView = new JRadioButton("角度差分表示");
		group5.add(showDiffAngView);
		p6.add(showDiffAngView);
		showDiffLenView = new JRadioButton("長さ差分表示");
		group5.add(showDiffLenView);
		p6.add(showDiffLenView);
		showDiffVectorView = new JRadioButton("ベクトル差分表示");
		group5.add(showDiffVectorView);
		p6.add(showDiffVectorView);
		//高さを変更する
		sliderDiff = new JSlider(0, 85, 10);
		sliderDiff.setMajorTickSpacing(10);
		sliderDiff.setMinorTickSpacing(5);
		sliderDiff.setPaintTicks(true);
		sliderDiff.setLabelTable(sliderDiff.createStandardLabels(10));
		sliderDiff.setPaintLabels(true);
		diffText = new JLabel(" 高さ: " + sliderDiff.getValue());
		p6.add(sliderDiff);
		p6.add(diffText);

		

		
		//
		// パネル群のレイアウト
		//
	    tabbedpane.addTab("表示", p1);
		tabbedpane.addTab("ベクトル", p2);
		tabbedpane.addTab("渦度", p3);
		tabbedpane.addTab("流線", p4);
		tabbedpane.addTab("流線選択", p5);
		tabbedpane.addTab("差分", p6);
		this.add(tabbedpane);
		
		//
		// リスナーの追加
		//
		if (bl == null)
			bl = new ButtonListener();
		addButtonListener(bl);

		if (rbl == null)
			rbl = new RadioButtonListener();
		addRadioButtonListener(rbl);

		if (cbl == null)
			cbl = new CheckBoxListener();
		addCheckBoxListener(cbl);

		if (sl == null)
			sl = new SliderListener();
		addSliderListener(sl);
	}

	/**
	 * Canvasをセットする
	 * @param c Canvas
	 */
	public void setCanvas(Object c) {
		canvas = (Canvas) c;
	}


	/**
	 * Cursor Sensor の ON/OFF を指定するフラグを返す
	 * @return cursorSensorFlag
	 */
	public boolean getCursorSensorFlag() {
		return cursorSensorFlag;
	}


	/**
	 * ラジオボタンのアクションの検出を設定する
	 * @param actionListener ActionListener
	 */
	public void addRadioButtonListener(ActionListener actionListener) {
		viewRotateButton.addActionListener(actionListener);
		viewScaleButton.addActionListener(actionListener);
		viewShiftButton.addActionListener(actionListener);
	}

	/**
	 * ボタンのアクションの検出を設定する
	 * @param actionListener ActionListener
	 */
	public void addButtonListener(ActionListener actionListener) {
		openDataButton.addActionListener(actionListener);
		viewResetButton.addActionListener(actionListener);
		generateStreamlineButton.addActionListener(actionListener);
	}

	/**
	 * チェックボックスのアクションの検出を設定する
	 * @param actionListener ActionListener
	 */
	public void addCheckBoxListener(CheckBoxListener checkBoxListener) {
	}

	/**
	 * スライダのアクションの検出を設定する
	 * @param actionListener ActionListener
	 */
	public void addSliderListener(ChangeListener changeListener) {
		sliderX.addChangeListener(changeListener);
		sliderY.addChangeListener(changeListener);
		sliderZ.addChangeListener(changeListener);
	}
	
	/**
	 * ボタンのアクションを検知するActionListener
	 * @author itot
	 */
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton buttonPushed = (JButton) e.getSource();

			if (buttonPushed == openDataButton) {
				grid1 = FileReader.getGrid(url1);
				grid2 = FileReader.getGrid(url2);
				sliderX.setValue(10);
				sliderY.setValue(10);
				sliderZ.setValue(10);
				canvas.setGrid1(grid1);
				canvas.setGrid2(grid2);
				canvas.setStreamline1(null);
				canvas.setStreamline2(null);
			}
			
			if (buttonPushed == viewResetButton) {
				grid1.setStartPoint(10, 10, 10);
				grid2.setStartPoint(10, 10, 10);
				sliderX.setValue(10);
				sliderY.setValue(10);
				sliderZ.setValue(10);
				canvas.viewReset();
			}
			
			if (buttonPushed == generateStreamlineButton) {
				Streamline sl1 = new Streamline();
				Streamline sl2 = new Streamline();
				int eIjk[] = new int[3];
				int numg[] = grid1.getNumGridPoint();
				eIjk[0] = sliderX.getValue() * numg[0] / 100;
				eIjk[1] = sliderY.getValue() * numg[1] / 100;
				eIjk[2] = sliderZ.getValue() * numg[2] / 100;
				StreamlineGenerator.generate(grid1, sl1, eIjk, null);
				System.out.println("    target:" + grid1.intersectWithTarget(sl1)); 
				canvas.setStreamline1(sl1);
				StreamlineGenerator.generate(grid2, sl2, eIjk, null);
				System.out.println("    target:" + grid1.intersectWithTarget(sl2)); 
				canvas.setStreamline2(sl2);
				
			}
			
			canvas.display();
		}
	}


	/**
	 * ファイルダイアログにイベントがあったときに、対応するディレクトリを特定する
	 * @return ファイル
	 */
	String getDirectory() {
		JFileChooser dirChooser = new JFileChooser();
		dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int selected = dirChooser.showOpenDialog(container);
		if (selected == JFileChooser.APPROVE_OPTION) { // open selected
			return dirChooser.getSelectedFile().getAbsolutePath();
		} else if (selected == JFileChooser.CANCEL_OPTION) { // cancel selected
			return null;
		} 
		
		return null;
	}

	
	/**
	 * 拡張子がJPGであるファイルの名前一式を配列に確保して返す
	 */
	String[] getJpegFilenames(String dirname) {
	
		File directory = new File(dirname);
		String[] filelist = directory.list();
		int num = 0;
		for(int i = 0; i < filelist.length; i++) {
			if(filelist[i].endsWith("JPG") || filelist[i].endsWith("jpg"))
				num++;
		}
		
		String jpeglist[] = new String[num];
		num = 0;
		for(int i = 0; i < filelist.length; i++) {
			if(filelist[i].endsWith("JPG") || filelist[i].endsWith("jpg"))
				jpeglist[num++] = filelist[i];
		}
		
		return jpeglist;
	}
			
			
	/**
	 * ラジオボタンのアクションを検知するActionListener
	 * @author itot
	 */
	class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton buttonPushed = (JRadioButton) e.getSource();
			if (buttonPushed == viewRotateButton) {
				canvas.setDragMode(3);
			}
			if (buttonPushed == viewScaleButton) {
				canvas.setDragMode(1);
			}
			if (buttonPushed == viewShiftButton) {
				canvas.setDragMode(2);
			}

			canvas.display();
		}
	}

	/**
	 * チェックボックスのアクションを検知するItemListener
	 * @author itot
	 */
	class CheckBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			JCheckBox stateChanged = (JCheckBox) e.getSource();

			// 再描画
			canvas.display();
		}
	}

	/**
	 * スライダのアクションを検知するActionListener
	 * @author itot
	 */
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			int numg[] = grid1.getNumGridPoint();
			JSlider changedSlider = (JSlider) e.getSource();
			if (changedSlider == sliderX) {
				xText.setText(" よこ:" + sliderX.getValue());
				grid1.startPoint[0] = sliderX.getValue() * numg[0] / 100;
				grid2.startPoint[0] = sliderX.getValue() * numg[0] / 100;
			}
			else if (changedSlider == sliderY) {
				yText.setText(" たて:" + sliderY.getValue());
				grid1.startPoint[1] = sliderY.getValue() * numg[1] / 100;
				grid2.startPoint[1] = sliderY.getValue() * numg[1] / 100;
			}
			else if (changedSlider == sliderZ) {
				zText.setText(" たかさ:" + sliderZ.getValue());
				grid1.startPoint[2] = sliderZ.getValue() * numg[2] / 100;
				grid2.startPoint[2] = sliderZ.getValue() * numg[2] / 100;
			}
			canvas.display();
		}
	}
}
