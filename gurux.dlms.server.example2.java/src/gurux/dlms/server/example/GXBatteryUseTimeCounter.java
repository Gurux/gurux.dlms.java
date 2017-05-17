package gurux.dlms.server.example;

import gurux.common.AutoResetEvent;
import gurux.dlms.objects.GXDLMSRegister;

/**
 * This class is updating Battery Use Time Counter once per second.
 */
public class GXBatteryUseTimeCounter extends Thread {
    private GXDLMSRegister target;
    private AutoResetEvent closing;

    public GXBatteryUseTimeCounter(GXDLMSRegister value) {
        closing = new AutoResetEvent(false);
        target = value;
    }

    /**
     * @return Is thread closing.
     */
    public boolean isClosing() {
        return closing.waitOne(1);
    }

    public void setClosing(final boolean value) {
        if (value) {
            closing.set();
        } else {
            closing.reset();
        }
    }

    public void run() {
        int cnt = 0;
        do {
            try {
                // Value is reset.
                if (target.getValue() == null) {
                    cnt = 0;
                }
                target.setValue(++cnt);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } while (!closing.waitOne(1000));
    }
}
