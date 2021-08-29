package com.example.todo;

public class taskshow {
    private String name;
    private String data;
    private String deadline;
    private int donePic ;
    private int deletePic ;
    private int editPic ;
    private boolean status = false;


    public taskshow (String name ,String data,String deadline,int donePic ,int editPic ,int deletePic,boolean status )
    {
        this.name = name;
        this.data=data;
        this.deadline = deadline;
        this.deletePic  =deletePic;
        this.donePic = donePic ;
        this.editPic = editPic ;
        this.status = status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setDonePic(int donePic) {
        this.donePic = donePic;
    }

    public void setDeletePic(int deletePic) {
        this.deletePic = deletePic;
    }

    public void setEditPic(int editPic) {
        this.editPic = editPic;
    }

    public boolean getStatus() {
        return status;
    }
    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getDeadline() {
        return deadline;
    }

    public int getDonePic() {
        return donePic;
    }

    public int getDeletePic() {
        return deletePic;
    }

    public int getEditPic() {
        return editPic;
    }
}
