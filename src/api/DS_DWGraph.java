package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class DS_DWGraph implements directed_weighted_graph {
    private HashMap<Integer,node_data> myNodes;
    private HashMap<Integer,HashMap<Integer,edge_data>> InEdges;
    private int NumOfEdges;
    private HashMap<Integer,HashMap<Integer,edge_data>> OutEdges;
    private int MC;

    public DS_DWGraph(){
        myNodes = new HashMap<>();
        OutEdges= new HashMap<>();
        InEdges=new HashMap<>();

    }
    public DS_DWGraph(directed_weighted_graph g){
        myNodes= new HashMap<>();
        for (node_data n:g.getV() ) {
            addNode(new NodeData(n));
        }
        OutEdges=new HashMap<>();
        InEdges=new HashMap<>();
        for(node_data n: g.getV()){
            for (edge_data e:g.getE(n.getKey())) {
                connect(n.getKey(),e.getDest(),e.getWeight());
            }
        }
    }

    @Override
    public node_data getNode(int key) {
        return myNodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {

        return  OutEdges.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        if (!myNodes.containsKey(n.getKey())) {
            myNodes.put(n.getKey(), new NodeData(n));
            OutEdges.put(n.getKey(),new HashMap<Integer,edge_data>());
            InEdges.put(n.getKey(),new HashMap<Integer,edge_data>());
            MC++;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(src!=dest&&myNodes.containsKey(src)&&myNodes.containsKey(dest)){
            OutEdges.get(src).put(dest,new EdgeData(src,dest,w));
            InEdges.get(dest).put(src,new EdgeData(src,dest,w));
            MC++;
            NumOfEdges++;
        }

    }

    @Override
    public Collection<node_data> getV() {
        return myNodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
       if(myNodes.containsKey(node_id))
           return OutEdges.get(node_id).values();
       return null;
    }

    @Override
    public node_data removeNode(int key) {
        if (myNodes.containsKey(key)){
            for (int keyNei :OutEdges.keySet()){
                InEdges.get(keyNei).remove(key);
            }
            for (int keyNei :InEdges.keySet()){
                OutEdges.get(keyNei).remove(key);
            }
            NumOfEdges-= InEdges.remove(key).size();
            NumOfEdges-= OutEdges.remove(key).size();
            MC++;
            return myNodes.remove(key);
        }
        return null;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if(src!=dest&&myNodes.containsKey(src)&&myNodes.containsKey(dest)){
            NumOfEdges--;
            MC++;
            InEdges.get(dest).remove(src);
          return   OutEdges.get(src).remove(dest);
        }
            return null;
    }

    @Override
    public int nodeSize() {
        return myNodes.size();
    }

    @Override
    public int edgeSize() {
        return NumOfEdges;
    }

    @Override
    public int getMC() {
        return MC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DS_DWGraph graph = (DS_DWGraph) o;
        return NumOfEdges == graph.NumOfEdges &&
                Objects.equals(myNodes, graph.myNodes) &&
                Objects.equals(InEdges, graph.InEdges) &&
                Objects.equals(OutEdges, graph.OutEdges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myNodes, InEdges, NumOfEdges, OutEdges);
    }
}
