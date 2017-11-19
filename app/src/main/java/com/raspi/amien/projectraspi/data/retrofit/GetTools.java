package com.raspi.amien.projectraspi.data.retrofit;

import java.util.List;

/**
 * Created by amien on 10/06/17.
 */

public class GetTools {


    private List<Tool> tools=null;

    public GetTools(List<Tool> tools) {
        this.tools = tools;
    }

    public List<Tool> getTools() {
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }
}
