package com.raspi.amien.projectraspi.data.retrofit;

import java.util.List;

/**
 * Created by amien on 10/08/17.
 */

public class GetGuest {
    private List<Guest> guest=null;

    public GetGuest(List<Guest> guest) {
        this.guest = guest;
    }

    public List<Guest> getGuest() {
        return guest;
    }

    public void setGuest(List<Guest> guest) {
        this.guest = guest;
    }
}
