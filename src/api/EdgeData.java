package api;

import java.util.Objects;

public class EdgeData implements edge_data {

    int src;
    int dst;
    double weight;
    String info;
    int tag;

    @Override
    public String toString() {
        return "EdgeData{" +
                "src=" + src +
                ", dst=" + dst +
                ", weight=" + weight +
                '}';
    }

    public EdgeData(int src, int dst, double weight) {
        this.src = src;
        this.dst = dst;
        this.weight=Math.abs(weight);
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dst;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info=s;

    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag=t;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeData edgeData = (EdgeData) o;
        return src == edgeData.src &&
                dst == edgeData.dst &&
                Double.compare(edgeData.weight, weight) == 0;
    }

}
