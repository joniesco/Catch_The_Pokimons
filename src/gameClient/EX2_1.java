package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;




public class EX2_1 implements Runnable{
    private static MyFrame _win;
    private static Arena _ar;
    public HashMap<Integer,List<node_data>> Paths;

    public static void main(String[] a) {
        Thread client = new Thread(new EX2_1());
        client.start();
    }

    @Override
    public void run() {
        int scenario_num = 7;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String JsonGraph = game.getGraph();
        System.out.println(JsonGraph);



        try {
            PrintWriter pw = new PrintWriter(new File("JsonGraph.json"));
            pw.write((JsonGraph));
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        String pks = game.getPokemons();
        DWGraph_Algo graph_algo= new DWGraph_Algo();
        graph_algo.load("JsonGraph.json");
        directed_weighted_graph graph =graph_algo.getGraph() ;
        init(game,graph);

        game.startGame();

        Paths= new HashMap<>();

        String JsonAgents= game.getAgents();
        List<CL_Agent> log = Arena.getAgents(JsonAgents, graph);

        for (CL_Agent agent:log) {
            Paths.put(agent.getID(),new ArrayList<node_data>());
        }

        // System.out.println(game.getAgents());

        int ind=0;
        long dt=100;


        while(game.isRunning()) {
            moveAgents(game, graph);
            //System.out.println(game.getAgents());
            try {
                if(ind%1==0) {_win.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }
    /**
     * Moves each of the agents along the edge
     * @param game
     * @param
     */
    public   void moveAgents(game_service game, directed_weighted_graph graph) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, graph);
        _ar.setAgents(log);

        String fs =  game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);

        String JsonGraph = game.getGraph();

        String JsonAgents= game.getAgents();

        ArrayList<CL_Agent> Agents= Arena.getAgents(JsonAgents,graph);
        for(int i=0;i<log.size();i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();

            if (Paths.get(id).isEmpty()){
                List<node_data> myPath= destPath(graph,src,i,game);
                System.out.println(myPath);
                Paths.put(id,myPath);
                game.chooseNextEdge(id,myPath.get(0).getKey());
                dest=myPath.get(0).getKey();
                Paths.get(id).remove(0);
                System.out.println("Agent: "+id+", from "+src+  " turned to node: "+dest);

            }else {

                game.chooseNextEdge(id,Paths.get(id).get(0).getKey());
                dest=Paths.get(id).get(0).getKey();
                Paths.get(id).remove(0);
                System.out.println("Agent: "+id+", from "+src+  " turned to node: "+dest);
            }
//            if(dest==-1) {
//                dest = destNode(graph, src,i,game);
//
//                game.chooseNextEdge(ag.getID(), dest);
//                System.out.println("Agent: "+id+", from "+src+  " turned to node: "+dest);
//            }
        }
        game.move();
    }
    /**
     * a very simple random walk implementation!
     * @param g
     * @param src
     * @return
     */
    public int destNode(directed_weighted_graph g, int src,int agentNumber, game_service game) {
        Collection<edge_data> edges = g.getE(src);

        ArrayList<CL_Pokemon> PokemonOnSrcEdges= PokemonOnEdges(edges,game,g);


        Comparator<CL_Pokemon> comparator= new Comparator<CL_Pokemon>() {
            @Override
            public int compare(CL_Pokemon o1, CL_Pokemon o2) {

                return Double.compare(o2.getValue(),o1.getValue());
            }
        };

        PokemonOnSrcEdges.sort(comparator);

        if(PokemonOnSrcEdges.iterator().hasNext()) {
            //  System.out.println(PokemonOnSrcEdges.get(0).get_edge().getDest());
            return PokemonOnSrcEdges.get(0).get_edge().getDest();
        }
        else {
            DWGraph_Algo graph_algo= new DWGraph_Algo();
            graph_algo.init(g);

            ArrayList<CL_Pokemon> allPokemons = Arena.json2Pokemons(game.getPokemons());

            for(int a = 0;a<allPokemons.size();a++) {
                Arena.updateEdge(allPokemons.get(a),g);}

            allPokemons.sort(new Comparator<CL_Pokemon>() {
                @Override
                public int compare(CL_Pokemon o1, CL_Pokemon o2) {
                    double cost1= o1.getValue()/(graph_algo.shortestPathDist(src,o1.get_edge().getSrc())*(graph_algo.shortestPath(src,o1.get_edge().getSrc()).size()));
                    double cost2= o2.getValue()/(graph_algo.shortestPathDist(src,o2.get_edge().getSrc())*(graph_algo.shortestPath(src,o2.get_edge().getSrc()).size()));

                    return Double.compare(cost2,cost1);
                }
            });


            CL_Pokemon destPokemon= allPokemons.get(agentNumber);

            int destNodePokemon= destPokemon.get_edge().getSrc();

            return graph_algo.shortestPath(src,destNodePokemon).get(1).getKey();
        }

    }

    public List<node_data> destPath(directed_weighted_graph g, int src,int agentNumber, game_service game){
        DWGraph_Algo graph_algo= new DWGraph_Algo();
        graph_algo.init(g);

        ArrayList<CL_Pokemon> allPokemons = Arena.json2Pokemons(game.getPokemons());


        for (int a = 0; a < allPokemons.size(); a++) {
            Arena.updateEdge(allPokemons.get(a), g);
        }

        Collection<edge_data> edges = g.getE(src);

        ArrayList<CL_Pokemon> PokemonOnSrcEdges= PokemonOnEdges(edges,game,g);


        Comparator<CL_Pokemon> comparator= new Comparator<CL_Pokemon>() {
            @Override
            public int compare(CL_Pokemon o1, CL_Pokemon o2) {

                return Double.compare(o2.getValue(),o1.getValue());
            }
        };

        PokemonOnSrcEdges.sort(comparator);

        if(PokemonOnSrcEdges.iterator().hasNext()) {
            List<node_data> path =graph_algo.shortestPath(src,PokemonOnSrcEdges.get(0).get_edge().getDest());
            path.remove(0);
            return path ;

        }else {

            allPokemons.sort(new Comparator<CL_Pokemon>() {
                @Override
                public int compare(CL_Pokemon o1, CL_Pokemon o2) {
                    double cost1 = o1.getValue() / (graph_algo.shortestPathDist(src, o1.get_edge().getSrc()) * (graph_algo.shortestPath(src, o1.get_edge().getSrc()).size()));
                    double cost2 = o2.getValue() / (graph_algo.shortestPathDist(src, o2.get_edge().getSrc()) * (graph_algo.shortestPath(src, o2.get_edge().getSrc()).size()));

                    return Double.compare(cost2, cost1);
                }
            });

            ArrayList<Integer> srcPokemon = new ArrayList<>();

            for (CL_Pokemon pk : allPokemons) {
                srcPokemon.add(pk.get_edge().getSrc());
            }

            srcPokemon = removeDuplicates(srcPokemon);

            int FinalDestNode = srcPokemon.get(agentNumber);// maybe out of bound

            List<node_data> path =graph_algo.shortestPath(src, FinalDestNode);
            path.remove(0);

            return path;

        }

    }



    public ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {

            ArrayList<Integer> newList = new ArrayList<>();
            for (Integer a : list) {
                if (!newList.contains(a)) {

                    newList.add(a);
                }
            }
            return newList;
        }


        public ArrayList<CL_Pokemon> PokemonOnEdges(Collection<edge_data> edges, game_service game, directed_weighted_graph g  ){
        ArrayList<CL_Pokemon> PokemonOnEdges = new ArrayList<>();

        ArrayList<CL_Pokemon> allPokemons = Arena.json2Pokemons(game.getPokemons());

        for(int a = 0;a<allPokemons.size();a++) {
            Arena.updateEdge(allPokemons.get(a),g);}

        for(CL_Pokemon pk:allPokemons ){
            if (edges.contains(pk.get_edge()))
                PokemonOnEdges.add(pk);
        }
        return PokemonOnEdges;

    }

    private void init(game_service game,directed_weighted_graph graph) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = graph;
        //gg.init(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _ar.setGame(game);
        _win = new MyFrame(_ar);
        _win.setSize(1000, 700);


        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon

            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());


            for(int a = 0;a<cl_fs.size();a++) {
                Arena.updateEdge(cl_fs.get(a),gg);}

//            for (CL_Pokemon pk :cl_fs)
//                System.out.println(pk.get_edge());

            Comparator<CL_Pokemon> comparator= new Comparator<CL_Pokemon>() {
                @Override
                public int compare(CL_Pokemon o1, CL_Pokemon o2) {
                    return Double.compare(o2.getValue(),o1.getValue());
                }
            };

            cl_fs.sort(comparator);

            for(int a = 0;a<rs;a++) {
                int nodeLocation=cl_fs.get(a).get_edge().getSrc();
                game.addAgent(nodeLocation);


            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }
}
