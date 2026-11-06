package tuples;

public record Triplet<A, B, C>(A first, B second, C third) {
    @Override
    public String toString() {
        return "(" + first + "," + second + "," + third + ")";
    }
}
