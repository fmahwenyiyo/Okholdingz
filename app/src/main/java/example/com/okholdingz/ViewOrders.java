package example.com.okholdingz;

/**
 * Created by kuda on 3/29/2016.
 */
public class ViewOrders {
    private  String code, name, qnty,price;

    public ViewOrders(String code, String name, String qnty, String price) {
        this.setCode(code);
        this.setName(name);
        this.setQnty(qnty);
        this.setPrice(price);


    }
public ViewOrders(){

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQnty() {
        return qnty;
    }

    public void setQnty(String qnty) {
        this.qnty = qnty;
    }





}
