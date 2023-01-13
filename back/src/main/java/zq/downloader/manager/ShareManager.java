package zq.downloader.manager;

import org.springframework.stereotype.Component;
import zq.downloader.bean.SharedTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ShareManager {

    private final Map<String, SharedTask> taskMap = new HashMap<>();

    public void addShare(SharedTask task) {
        taskMap.put(task.getUUID(), task);
        checkAllExpired();
    }

    public SharedTask findTaskByUUID(String uuid) {
        checkAllExpired();
        return taskMap.get(uuid);
    }

    public void checkAllExpired() {
        List<SharedTask> shouldRemoveList = new ArrayList<>();
        taskMap.forEach((k, v) -> {
            if (v.isExpired()) shouldRemoveList.add(v);
        });
        for (SharedTask t : shouldRemoveList) {
            taskMap.get(t.getUUID()).getTask().setShared(false);
            taskMap.remove(t.getUUID());
        }
    }

}
