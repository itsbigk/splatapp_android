package tk.rockbutton.splatapp;

import java.util.List;

/**
 * Created by max on 10/15/15.
 */
public class MapRotation {
    public long updateTime;
    public List<ScheduleItem> schedule;

    public MapRotation() {
    }

    public MapRotation(int _updateTime, List<ScheduleItem> _schedule) {
        updateTime = _updateTime;
        schedule = _schedule;
    }

    public class ScheduleItem {
        public long startTime;
        public long endTime;
        public MapItem[] regular = new MapItem[]{
                new MapItem(),
                new MapItem()};
        public MapItem[] ranked = new MapItem[]{
                new MapItem(),
                new MapItem()};
        public String rankedRulesJp;
        public String rankedRulesEn;

        public class MapItem {
            public String nameJp;
            public String nameEn;
        }
    }
}
