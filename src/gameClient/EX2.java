package gameClient;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


public class EX2 implements Runnable{
    private static MyFrame _win;
    private static Arena _ar;

    public static void main(String[] a) {
        Thread client = new Thread(new EX2());
        client.start();
    }

    @Override
    public void run() {
        int scenario_num = 11;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        init(game);

        game.startGame();

       // System.out.println(game.getAgents());

        int ind=0;
        long dt=100;


        while(game.isRunning()) {
          moveAgants(game, gg);
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
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     * @param game
     * @param gg
     * @param
     */
    private  void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs =  game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        for(int i=0;i<log.size();i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if(dest==-1) {
                dest = nextNode(gg, src,game);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
            }
        }
    }
    /**
     * a very simple random walk implementation!
     * @param g
     * @param src
     * @return
     */
    public int nextNode(directed_weighted_graph g, int src, game_service game) {
      Collection<edge_data> edges = g.getE(src);

      ArrayList<CL_Pokemon> PokemonOnSrcEdges= PokemonOnEdges(edges,game);

        Comparator<CL_Pokemon> comparator= new Comparator<CL_Pokemon>() {
            @Override
            public int compare(CL_Pokemon o1, CL_Pokemon o2) {
                return Double.compare(o2.getValue(),o1.getValue());
            }
        };

        PokemonOnSrcEdges.sort(comparator);

        return PokemonOnSrcEdges.get(0).get_edge().getDest();

    }

    public ArrayList<CL_Pokemon> PokemonOnEdges(Collection<edge_data> edges, game_service game  ){
        ArrayList<CL_Pokemon> PokemonOnEdges = new ArrayList<>();

        ArrayList<CL_Pokemon> allPokemons = Arena.json2Pokemons(game.getPokemons());
        for(CL_Pokemon pk:allPokemons ){
           if (edges.contains(pk.get_edge()))
               PokemonOnEdges.add(pk);
        }
       return PokemonOnEdges;

    }

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
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

            for (CL_Pokemon pok:cl_fs) {
                System.out.println( "value:"+ pok.getValue()+", src:"+pok.get_edge().getSrc());
            }

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
                System.out.println(nodeLocation);


            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }
}
