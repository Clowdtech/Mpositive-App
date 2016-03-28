package clowdtech.mpositive.areas.till.contracts;

public interface IProductLineSelection<TV, TVM> {
    void incrementLine(TVM model);

    void decrementLine(TV view, TVM model);
}
