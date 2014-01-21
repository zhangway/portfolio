package no.uit.zhangwei.runkeeper.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import no.uit.zhangwei.RunKeeperDAO;
import no.uit.zhangwei.Model.runkeeper.Activity;

public class JdbcRunKeeperDAO implements RunKeeperDAO {
	
	private DataSource dataSource;
	 
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void insert(Activity activity) {
		String sql = "INSERT INTO running " +
				"(timestamp, source, activity, activityID, uri, duration, calories, climb, total_distance, total_calories, type, userID) VALUES " +
				" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection conn = null;
 
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setTimestamp(1, activity.getTimeStamp());
			ps.setString(2, activity.getSource());
			ps.setString(3, activity.getActivity());
			ps.setString(4, activity.getActivityID());
			ps.setString(5, activity.getUri());
			ps.setBigDecimal(6, activity.getDuration());
			ps.setInt(7, activity.getCalories());
			ps.setBigDecimal(8, activity.getClimb());
			ps.setBigDecimal(9, activity.getTotalDistance());
			ps.setBigDecimal(10, activity.getTotalCalories());
			ps.setString(11, activity.getType());
			ps.setString(12, activity.getUserID());
			ps.executeUpdate();
			ps.close();
 
		} catch (SQLException e) {
			throw new RuntimeException(e);
 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}

	}

	@Override
	public Activity findByTimestamp(Timestamp timeStamp) {
		// TODO Auto-generated method stub
		return null;
	}

}
