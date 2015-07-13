package com.yuanwei.resistance.model;

import com.yuanwei.resistance.model.protocol.Event;

/**
 * Created by chenyuanwei on 15/7/12.
 */
public enum GameEvent implements Event {
    PROPOSE {
        @Override
        public String getEventType() {
            return this.name();
        }
    },

    PROPOSE_VETO {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    PROPOSE_PASSED {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    MISSION_EXECUTION_START {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    MISSION_EXECUTION_CONTINUE {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    MISSION_EXECUTION_COMPLETED {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    MISSION_SUCCEED {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    MISSION_FAILED {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    SHOW_RESULTS {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    VOTE {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    RESISTANCE_WIN {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    SPY_WIN {
        @Override
        public String getEventType() {
            return this.name();
        }
    },
    RESTART {
        @Override
        public String getEventType() {
            return this.name();
        }
    };
}
