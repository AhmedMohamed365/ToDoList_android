package com.example.todo;

public class taskshow {
    private String name;
    private String date;
    private int donePic ;
    private int deletePic ;
    private int editPic ;
    public taskshow (String name ,String date,int donePic ,int editPic ,int deletePic )
    {
        this.name = name;
        this.date=date;
        this.deletePic  =deletePic;
        this.donePic = donePic ;
        this.editPic = editPic ;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
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
