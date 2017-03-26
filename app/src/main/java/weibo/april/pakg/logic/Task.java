package weibo.april.pakg.logic;

import java.util.Map;

/**
 * Created by Duke on 3/25/2017.
 */

public class Task {
    //define all the kinds of tasks that we need to process
    public static final int WEIBO_LOGON =1;
    // fields of Task
    private int taskId;
    private Map<String,Object> taskParams;
    //constructor for Task
    public Task(int taskId, Map<String, Object> taskParams) {
        this.taskId = taskId;
        this.taskParams = taskParams;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Map<String, Object> getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(Map<String, Object> taskParams) {
        this.taskParams = taskParams;
    }
}
