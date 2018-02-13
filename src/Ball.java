import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Ball {

    private String center;
    private Set<String> coveredPoints;

    public Ball(String center, Set<String> coveredPoints) {
        this.center = center;
        this.coveredPoints = coveredPoints;
    }

    public Ball(String center) {
        this(center, new HashSet<>());
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public Set<String> getCoveredPoints() {
        return coveredPoints;
    }

    public void setCoveredPoints(Set<String> coveredPoints) {
        this.coveredPoints = coveredPoints;
    }

    public void generatePoints(final Set<String> set, final Integer radius) {
        Set<String> points = set.stream().filter(a -> Utils.hammingDistance(a, this.getCenter()) <= radius).collect(Collectors.toCollection(HashSet::new));
        this.setCoveredPoints(points);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ball ball = (Ball) o;

        return center != null ? center.equals(ball.center) : ball.center == null;
    }

    @Override
    public int hashCode() {
        return center != null ? center.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Ball{" +
                "center='" + center + '\'' +
                ", coveredPoints=" + coveredPoints +
                '}';
    }
}
