/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inet.encode.bean;

import inet.key.bean.JsonBase;

/**
 *
 * @author dainv
 */
public class MessageResponse extends JsonBase<MessageResponse> {

    // Status: 0 - success, 1... - error
    private int status;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
