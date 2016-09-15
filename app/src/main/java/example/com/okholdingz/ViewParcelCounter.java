package example.com.okholdingz;

/**
 * Created by kuda on 3/29/2016.
 */
public class ViewParcelCounter {
    private  String sid, status, booker;

    public ViewParcelCounter(String sid, String status, String booker) {
        this.setSid(sid);
        this.setStatus(status);
        this.setBooker(booker);



    }
public ViewParcelCounter(){

    }


    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBooker() {
        return booker;
    }

    public void setBooker(String booker) {
        this.booker = booker;
    }





}
