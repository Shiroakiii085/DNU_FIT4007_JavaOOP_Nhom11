package services.interfaces;

public interface DiscountPolicy {
    double applyDiscount(double originalPrice);
    String getDiscountDescription();
}
