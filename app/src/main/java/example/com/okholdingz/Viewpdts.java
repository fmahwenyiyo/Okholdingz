package example.com.okholdingz;

/**
 * Created by kuda on 3/29/2016.
 */
public class Viewpdts {
    private  String code, name, type,price;

    public Viewpdts(String code, String name, String type,String price) {
        this.setCode(code);
        this.setName(name);
        this.setType(type);
        this.setPrice(price);


    }
public Viewpdts(){

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }





}
