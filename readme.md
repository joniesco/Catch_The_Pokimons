# EX_2:

## public class NodeData implements node_data {
This  class represents a node_data which is a vertex in a weighted-directed  graph that contains few fields:  key, info ,geo location, weight and tag. Each node can have  vertices “neighbors” which means there are weighted edges(a unergative weight) that connect it to each one of these vertices .
The fields of this class are:

- Key (int): Represents the key of the node.

- g (geo location):  represent the geo location of the node

- weight (double ):  represent the weight of the node

- Info (String): Represent the info (meta data) of the no

- Tag (int): A temp variable, can be used for some markings and calculation of algorithms.

### Methods:
-get (for all the fields)

-set(for all the fields)

-to string(key and tag)

-equals(checking if two nodes are the same by the key) 
 
## public class EdgeData implements edge_data:
This  class represents a edge data which is a object in a weighted-directed  graph that connect between two nodes. Each edge have a source (which he comes from) and a destination(which he goes to).edge data have few fields:

-src(int): where the edge comes from 

-dest(int)to where the edge goes to

-weight(double)-the weight of the edge

-info(string)

-tag(int)

### Methods:
-get (for all the fields)

-set(for all the fields)

-to string(src,dst and weight)

-equals(checking if two edges are the same by src , dest and weight)  

## public class DS_DWGraph implements directed_weighted_graph:
This class represents an directed, weighted   graph, which is a collection of nodes/vertex, the nodes can be connected to each other, but not necessarily.each edge has source dest and weight 

The fields of this class are:
- MyNodes (HashMap<Integer, node_data>): A HashMap of data nodes which represent our graph, in form of node_key -> node_data.
- HashMap<Integer,HashMap<Integer,edge_data>> InEdges: represent us the inner edges in the graph(from which node it arrived). In the inner hash map the key is the neighbor and value is the edge
- HashMap<Integer,HashMap<Integer,edge_data>> OutEdges:  represent us the external edges in the graph(to which node it goes).in the inner hash map the key is the neighbor and value is the edge
- NumOfEdge (int): Represents the number of edges that the graph contains.
- MC (int): Represents the number of operations that were made on the graph. Each update on the graph, will raise MC by one. Initial value is zero.
### Methods:
Adding/removing a node to/from the graph, connecting between nodes (by weighted edge and by source-dest ) in the graph. Get size (num of nodes), specific node, MC, list of graph’s nodes and list of neighbors. Also, getEdge which give us information about the edge between nodes. removing a edge from the graph. The function equals chek if tow graph are equals by they struct and there information..

## Class Graph_Algo implements graph_algorithms

This class represents some algorithms on the graph that will help us to get some information.
The fields of this class are:
-gAlgo (graph): Represents our actual graph.

### Methods and algorithms:

 #### isConnected:
The method will determine if the graph “is strongly connected” – a graph is connected if and only if there is a valid path from every node to each other node. The method will first check some basic cases such as empty graph, and after it will use the DfsAlgo which will retrieve the number of all the visited nodes from a given source in graph - here source will be the first node in graph node list (getting by using iterator). isConnected will return true only if there is only one component in the graph using a transpose graph and compering there components.

#### runDfsAlgo:
Helping data structure: stack.
In this method we will use the DFS algorithm. The method gets a source to start from and will calculate all the possible paths from it using the DFS and the neighbors list. The DFS will be implemented by using a stack which will contain all the visited vertex and will always handle first the last visited vertex (DFS principle). To mark a visited node, the algorithm will use the tag in each node – 1 will represent a visited node, and 0 will represent a non-visited one. it will reset all nodes tags to 0 (resetTags), so it’ll can be use in other run.

#### shortestPath list:
The method retrieves a list of vertices which represents a path in the graph from a given source and destination, if exists. The method will first check some basic cases, such as same source and destination node, or if one of these doesn’t exists in the graph. And then using the dijakstra algorithm  .

#### shortestPathDist:
The method will retrieve the size of the path between a given source and destination. The method will  check basic cases and than will use shortestPath and will just return the tag of the dest node  which will represent the distance  (the total weight) to get from the source node to the distance node.



#### runDijkstraAlgo:

Helping data structure: priorityqueue, HashMap<Integer, WGraph_DS.Node_info> prev
 
This method help us to calculate the smallest weight to get from a given node to another given node. First all node tags initialize to infinity and node ingo to "black" , than we start with the source node and update his tag to 0. And putting this node into the hashmap first the value of src is null (he have no predecessor)
And finally we will add this node into our priority queue.
Starting of the loop: while our queue is not empty 
We will make a remove from the queue to a  temporary node (current node)
If there is more than one node in the queue the first to get out is the one with the lowest tag as we defined it 
Now we will pass over all the node neighbors  . and we will check if  the neighbor wasn’t visited: we will check the edge weight , and if the edge weight of the neighbor plus the tag of the current node is less then the neighbor's tag so update the tag of the neighbor    and put him into the map , the key is the node key and the value is the current node so we will know from where we arrived . and finally after the loop ends we will have a list of nodes. In each node we can know how we arrived to him(via who) and how much it cost us (weight) by the node tags.

Load:
This method load a graph to this graph algorithm

Save:
this method Saves this weighted  graph to the given file name

#### The second part of the project:
about the second part of the project which contains the cath_pokemons game you can read in the wiki page that attched to this project


