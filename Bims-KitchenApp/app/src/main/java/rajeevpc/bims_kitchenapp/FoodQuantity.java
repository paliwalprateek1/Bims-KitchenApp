package rajeevpc.bims_kitchenapp;

/**
 * Created by prateek on 6/11/16.
 */
public class FoodQuantity {

    private String food, price, quantity;

    public FoodQuantity() {
    }

//        public Food(String food, String price) {
//            this.food = food;
//            this.price = price;
//        }

    public FoodQuantity(String food, String price, String quantity){
        this.food = food;
        this.price = price;
        this.quantity = quantity;

    }

    public String getFood() {
        return food;
    }

    public void setFood(String name) {
        this.food = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity(){ return quantity;}

    public void setQuantity(String quantity){ this.quantity = quantity;}
}
