package com.raspi.amien.projectraspi.data.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amien on 10/06/17.
 */

public class Tool {
    @SerializedName("no")
    @Expose
    private String no;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;

    public Tool(String no, String status) {
        this.no = no;
        this.status = status;
    }

    public Tool(String no, String name, String status) {
        this.no = no;
        this.name = name;
        this.status = status;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
