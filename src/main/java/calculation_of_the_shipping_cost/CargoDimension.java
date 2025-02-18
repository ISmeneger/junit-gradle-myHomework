package calculation_of_the_shipping_cost;

public enum CargoDimension {
    LARGE(200),
    SMALL(100);

    private int costIncrease;

    CargoDimension(int costIncrease) {
        this.costIncrease = costIncrease;
    }

    public int getCostIncrease() {
        return costIncrease;
    }
}
