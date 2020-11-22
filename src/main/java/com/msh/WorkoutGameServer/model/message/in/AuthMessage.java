package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.message.MessageType;
import com.msh.WorkoutGameServer.model.user.LoginUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthMessage extends GameMessage {

    private LoginUser user;

    public AuthMessage() {
    }

    public AuthMessage(MessageType type, String from, String text, LoginUser user) {
        super(type, from, text);
        this.user = user;
    }

    @Override
    public String toString() {
        return super.toString() +
                " user=" + user;
    }
}
