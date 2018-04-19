package com.ouchgzee.study.web.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloWorld {
	public static void main(String[] args) {
		Connection connection = null;
		Statement statement = null;
		try {
			String url = "jdbc:postgresql://rm-wz94h1kw3xfcl91blso.ppas.rds.aliyuncs.com:3432/gjt_gxlclms";
			String user = "admintest";
			String password = "Edu789987";
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("是否成功连接pg数据库" + connection);
			String sql = "SELECT * FROM GJT_TERM_INFO";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String name = resultSet.getString(1);
				System.out.println(name);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
}