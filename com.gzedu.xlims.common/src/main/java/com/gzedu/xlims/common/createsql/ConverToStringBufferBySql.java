package com.gzedu.xlims.common.createsql;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

@SuppressWarnings("serial")
public class ConverToStringBufferBySql extends JFrame {

	private JPanel jPanel;
	private JSplitPane jSplitPane;
	private JTextArea leftSql;
	private JTextArea rightSql;


	

	/**
	 * Create the frame.
	 */
	public ConverToStringBufferBySql() {
		setMinimumSize(new Dimension(840, 600));
		setTitle("SQL 转 StringBuffer 拼接");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 842, 605);
		jPanel = new JPanel();
		jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(jPanel);
		jPanel.setLayout(new BorderLayout(0, 0));

		JPanel jPanel1 = new JPanel();
		jPanel1.setPreferredSize(new Dimension(10, 80));
		jPanel.add(jPanel1, BorderLayout.NORTH);
		jPanel1.setLayout(new BorderLayout(0, 0));

		JPanel jPanel2 = new JPanel();
		jPanel2.setBorder(new LineBorder(new Color(0, 0, 0)));
		jPanel2.setPreferredSize(new Dimension(300, 10));
		jPanel1.add(jPanel2, BorderLayout.CENTER);
		jPanel2.setLayout(null);

		JPanel jPanel3 = new JPanel();
		jPanel3.setBorder(new MatteBorder(1, 0, 1, 1, (Color) new Color(0, 0, 0)));
		jPanel3.setPreferredSize(new Dimension(200, 10));
		jPanel1.add(jPanel3, BorderLayout.EAST);
		jPanel3.setLayout(new BorderLayout(0, 0));

		JPanel jPanel4 = new JPanel();
		jPanel4.setBorder(new MatteBorder(0, 1, 1, 1, (Color) new Color(0, 0, 0)));
		jPanel.add(jPanel4, BorderLayout.CENTER);
		jPanel4.setLayout(new BorderLayout(0, 0));

		jSplitPane = new JSplitPane();
		jSplitPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				jSplitPane.setDividerLocation(0.4);
			}
		});
		jPanel4.add(jSplitPane, BorderLayout.CENTER);

		JScrollPane jScrollPane = new JScrollPane();
		jSplitPane.setLeftComponent(jScrollPane);

		leftSql = new JTextArea();
		jScrollPane.setViewportView(leftSql);

		JScrollPane jScrollPane2 = new JScrollPane();
		jSplitPane.setRightComponent(jScrollPane2);

		rightSql = new JTextArea();
		jScrollPane2.setViewportView(rightSql);

		JPanel jPanel5 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) jPanel5.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		jPanel5.setPreferredSize(new Dimension(10, 30));
		jPanel4.add(jPanel5, BorderLayout.NORTH);

		JLabel jLabel = new JLabel("请在左侧输入你要格式化的SQL语句：");
		jLabel.setHorizontalAlignment(SwingConstants.LEFT);
		jPanel5.add(jLabel);
		
		JButton jButton = new JButton("生成");
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String leftSqlStr = leftSql.getText();
				if (leftSql.equals("")) {
					JOptionPane.showMessageDialog(ConverToStringBufferBySql.this, "请在左侧输入SQL！^_^");
					return;
				}
				if (!rightSql.getText().equals("")) {
					rightSql.setText("");
				}
				String params = "sql";
				if ("".equals(params.trim())) {
					JOptionPane.showMessageDialog(ConverToStringBufferBySql.this, "请输入变量参数^_^！");
					return;
				}
				String[] sqls = leftSqlStr.split("\n");
				StringBuffer result = new StringBuffer();
				for (int i = 0; i < sqls.length; i++) {
					result.append(params + ".append(\" " + sqls[i] + " \");\n");
				}
				rightSql.setText(result.toString());
			}
		});
		jButton.setFont(new Font("宋体", Font.PLAIN, 32));
		jPanel3.add(jButton, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConverToStringBufferBySql frame = new ConverToStringBufferBySql();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
