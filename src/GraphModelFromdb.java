//Import required packages
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GraphModelFromdb {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/structureddiscussion";

   //  Database credentials
   static final String USER = "dataadmin";
   static final String PASS = "sd_database1";
   
   //To create graph object
   static Graph<String> graph = new Graph<String>();
   
   //To create vertex , edge objects
   static ArrayList<Vertex<String>> vertexArrLst = new ArrayList<Vertex<String>>();
   static ArrayList<Edge<String>> edgArrLst = new ArrayList<Edge<String>>();
   
   //To create list of vertices to be included in subgraph
   static List<Vertex<String>> subgraphverticies = new ArrayList<Vertex<String>>();
   
   static Graph<String> subgraph;
   
   static List<Vertex<String>> bfsVertices ;
   
   static List<Vertex<String>> dfsVertices ;
   
   static int bellmanFordSource ;
   
   static Map<String,Integer> bellmanFordResult ;
   
   static int djikstraSource ;
   
   static Map<String,Integer> djikstraResult ;
   
   static int[][] floydWarshallResult ;
   
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   Statement stmt2 = null;
   Statement stmt3 = null ;
   Statement stmt4 = null ;
   Statement stmt5 = null ;
   
   
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      stmt2 = conn.createStatement();
      stmt3 = conn.createStatement();
      stmt4 = conn.createStatement();
      stmt5 = conn.createStatement();
      String sql , sql2 ,sql3 , sql4, sql5;
      sql = "SELECT * FROM vertex";
      sql2 = "SELECT * FROM edge";
      sql3 = "SELECT vertex_id FROM vertex where vertex_type = 'Stance' ";
      ResultSet rs = stmt.executeQuery(sql);
      ResultSet rs2 = stmt2.executeQuery(sql2);
      ResultSet rs3 = stmt3.executeQuery(sql3);

      //STEP 5: Extract data from result set
      while(rs.next()){
         //Retrieve by column name
         String id  = rs.getString("vertex_id");
         String type  = rs.getString("vertex_type");
         String data = rs.getString("vertex_data");
         String note  = rs.getString("vertex_note");
         String authority = rs.getString("vertex_authority");
         String trust = rs.getString("vertex_trust");
         String rating = rs.getString("vertex_rating");
         //add information to each vertex object
         vertexArrLst.add(new Vertex<String>(id,type,data,note,authority,trust,rating));   
      }
      
      //add each vertex to graph
      Iterator<Vertex<String>>  iterator = vertexArrLst.iterator();
		while (iterator.hasNext()) {
			graph.addVertex(iterator.next());
		}
	
	  rs.close();
			
	  while(rs2.next()){
	        //Retrieve by column name
			String from  = rs2.getString("edge_from");
			String to  = rs2.getString("edge_to");	
			int cost = rs2.getInt("cost");
			String connection = rs2.getString("edge_connection");
	        for (Vertex<String> temp : vertexArrLst )
	         {
	        	 String fromVertex = temp.name ;
	        	 	        	 
	        	 for(Vertex<String> temp2 : vertexArrLst)
	        	 {
	        		 String toVertex = temp2.name ;
	        		 
		 			 if ((fromVertex.equals(from)) && (toVertex.equals(to)))
		 			  {
		 			   edgArrLst.add(new Edge<String>(temp,temp2,cost,connection)); 
		 			   graph.addEdge(temp,temp2,cost,connection);
		 		     }
		         }
	         
	      }	
		}
		 
	  
	  rs2.close();   
	
	  //STEP 6: Performing tasks and displaying information
	  System.out.println("The vertices information is :");
	  System.out.println("The number of vertices is :"+graph.verticessize());
	  System.out.println(graph.getVertices()) ;
	  
	  System.out.println("---------------------------------------------");
	  
	  System.out.println("The edges information is :");
	  System.out.println("The number of edges is :"+graph.edgessize());
	  System.out.println(graph.getEdges()) ;
	  
	  System.out.println("---------------------------------------------");
	  
	  while(rs3.next())
	  {
		  String stance  = rs3.getString("vertex_id");
		  for (Vertex<String> temp : vertexArrLst )
	         {
			     
	        	 if(stance.equals(temp.name))
	        	 {
	        		 System.out.println("Breadth first search of"+stance);
	        		 bfsVertices = graph.breadthFirstSearch(temp);
	        		 for(Vertex<String> vertex : bfsVertices)
	        		 {
	        			 System.out.println(vertex);
	        		 }
	        		 bfsVertices.clear();
	        		 System.out.println("---------------------------------------------");
	        		 System.out.println("Depth first search of"+stance);
	        		 dfsVertices = graph.depthFirstSearch(temp);
	        		 for(Vertex<String> vertex : dfsVertices)
	        		 {
	        			 System.out.println(vertex);
	        		 }
	        		 dfsVertices.clear();
	        		 System.out.println("---------------------------------------------");
	        		 System.out.println("Bellman ford from:"+stance);
	        		 bellmanFordSource = graph.getVertices().indexOf(temp);
	        		 bellmanFordResult = graph.BellmanFord(graph, bellmanFordSource);
	        		 System.out.println(bellmanFordResult);
	        		 System.out.println("---------------------------------------------");
	        		 System.out.println("Djikstra from:"+stance);
	        		 djikstraSource = graph.getVertices().indexOf(temp);
	        		 djikstraResult = graph.djikstra(graph,djikstraSource);
	        		 System.out.println(djikstraResult);
	        		 System.out.println("---------------------------------------------");
	        	 }
	         }
	  }
	  
	  rs3.close();
	  
	  	  
	  //To form subgraph for yes stance
	  sql4 = "SELECT vertex_id FROM vertex where vertex_related_stance = 'Yes'" ;
	  		 		
	  ResultSet rs4 = stmt4.executeQuery(sql4);
	  
	  while(rs4.next())
	  {
		  String id = rs4.getString("vertex_id");
		  for (Vertex<String> temp : vertexArrLst )
	         {
			     String vertex_name = temp.name ;
	        	 if(vertex_name.equals(id))
	        	 {
	        		 subgraphverticies.add(temp);
	        	 }
	         }
	  }
	  
	  subgraph = graph.Subgraph(graph, subgraphverticies);
	  
	  System.out.println("SUBGRAPH for Stance Yes");

	  System.out.println("The number of vertices in graph is:" + subgraph.verticessize());
	  System.out.println("The information about vertices");
	  System.out.println(subgraph.getVertices());
	  
	  System.out.println("--------------------------");
	  
	  System.out.println("The number of edges in graph is:" + subgraph.edgessize());
	  System.out.println("The information about edges");
	  System.out.println(subgraph.getEdges());
	  
	  System.out.println("---------------------------------------------");
	  
	  //To form subgraph for No stance
	  sql5 = "SELECT vertex_id FROM vertex where vertex_related_stance = 'No' ";
	  ResultSet rs5 = stmt5.executeQuery(sql5);
	  
	  subgraphverticies.clear();
	  
	  while(rs5.next())
	  {
		  String id = rs5.getString("vertex_id");
		  for (Vertex<String> temp : vertexArrLst )
	         {
			     String vertex_name = temp.name ;
	        	 if(vertex_name.equals(id))
	        	 {
	        		 subgraphverticies.add(temp);
	        	 }
	         }
	  }
	  
	  subgraph = graph.Subgraph(graph, subgraphverticies);
	  
	  System.out.println("SUBGRAPH for Stance No");

	  System.out.println("The number of vertices in graph is:" + subgraph.verticessize());
	  System.out.println("The information about vertices");
	  System.out.println(subgraph.getVertices());
	  
	  System.out.println("--------------------------");
	  
	  System.out.println("The number of edges in graph is:" + subgraph.edgessize());
	  System.out.println("The information about edges");
	  System.out.println(subgraph.getEdges());
	  
	  System.out.println("---------------------------------------------");
	  
	  
	  rs4.close();
	  rs5.close();
	  
	  floydWarshallResult = new int[graph.verticessize()][graph.verticessize()];
	  floydWarshallResult = graph.floydWarshall(graph);
	  System.out.println("Floyd Warshalls Algorithm:");
	  for (int i=0; i<graph.verticessize(); ++i)
      {
		  System.out.print(graph.verticies.get(i).name +":\t");
          for (int j=0; j<graph.verticessize(); ++j)
          {
                  System.out.print(floydWarshallResult[i][j]+"\t");
          }
          System.out.println();
      }
	  
	  System.out.println("---------------------------------------------");
	  
      //STEP 7: Clean-up environment
	  
      stmt.close();
      stmt2.close();
      stmt3.close();
      stmt4.close();
      stmt5.close();
      conn.close();
      
      
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Done!");
}//end main
}//end FirstExample
