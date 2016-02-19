package models;

import javax.validation.constraints.NotNull;

public class User{

    @NotNull
    public String name;

    public int age = 0;

    public UserInfo info;

    public UserInfo[] infos;
}
