package no.uit.zhangwei;

import java.sql.Timestamp;

import no.uit.zhangwei.Model.runkeeper.Activity;

public interface RunKeeperDAO {
	
	public void insert(Activity activity);
	public Activity findByTimestamp(Timestamp timeStamp);

}
