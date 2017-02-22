//Import required packages
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class GraphModelOutputToJson extends HttpServlet {
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
   
   Map<Integer, JSONObject> verticesMap = new HashMap<Integer, JSONObject>();
   Map<Integer, JSONObject> edgesMap = new HashMap<Integer, JSONObject>();
   
   Map<Integer, JSONObject> subgraphVerticesMap_yes = new HashMap<Integer, JSONObject>();
   Map<Integer, JSONObject> subgraphVerticesMap_no = new HashMap<Integer, JSONObject>();
   
   Map<Integer, JSONObject> subgraphEdgesMap_yes = new HashMap<Integer, JSONObject>();
   Map<Integer, JSONObject> subgraphEdgesMap_no = new HashMap<Integer, JSONObject>();
   
   public void doGet(HttpServletRequest request,HttpServletResponse response)
   throws ServletException, IOException{
   
   Connection conn = null;
   Statement stmt = null;
   Statement stmt2 = null;
   Statement stmt3 = null ;
   Statement stmt4 = null ;
   Statement stmt5 = null ;
   
   // Set response content type
   response.setContentType("application/json");

   // Actual logic goes here.
   PrintWriter out = response.getWriter();
   
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
		 			   //add edge to graph
		 			   graph.addEdge(temp,temp2,cost,connection);
		 		     }
		         }
	         
	      }	
		}
		 
	  
	  rs2.close();   
	
	  //STEP 6: Performing tasks and displaying information
	  System.out.println("The vertices information is :");
	  System.out.println("The number of vertices is :"+graph.verticessize());
	  
	  //creating JSONObject and putting information into it
	  int vertex_ = 1 ;
	  JSONObject vertexobj ;
	  for(Vertex<String> temp : graph.getVertices())
	  {
		  vertexobj = new JSONObject();
		  vertexobj.put("Vertex_name",temp.name);
		  vertexobj.put("Vertex_type",temp.type);
		  vertexobj.put("Vertex_data",temp.data);
		  vertexobj.put("Vertex_note",temp.note);
		  vertexobj.put("Vertex_authority",temp.authority);
		  vertexobj.put("Vertex_trust",temp.trust);
		  vertexobj.put("Vertex_rating",temp.rating);  
		  verticesMap.put(vertex_++ , vertexobj);
	  }
	  
	  //diplaying JSONObject
	  System.out.println(verticesMap);
	  out.println(verticesMap);
	  
	  System.out.println("---------------------------------------------");
	  
	  System.out.println("The edges information is :");
	  System.out.println("The number of edges is :"+graph.edgessize());
	  
	  //creating JSONObjects and putting information into it
	  int edge = 1 ;
	  JSONObject edgeobj ;
	  for(Edge<String> temp2 : graph.getEdges())
	  {
		  edgeobj = new JSONObject();
		  edgeobj.put("Edge_from",temp2.from);
		  edgeobj.put("Edge_to",temp2.to);
		  edgeobj.put("Edge_cost",temp2.cost);
		  edgeobj.put("Edge_connection",temp2.connection); 
		  edgesMap.put(edge++ , edgeobj);
	  }
	  
	  System.out.println(edgesMap);
	  out.println(edgesMap);
	  
	  System.out.println("---------------------------------------------");
	  
	  /*
	  while(rs3.next())
	  {
		  String stance  = rs3.getString("vertex_id");
		  
 		  JSONObject bfsVertexObject ;
 		  
		  JSONObject dfsVertexObject ;
		  
		  int count = 0 ;
		  
		  for (Vertex<String> temp : vertexArrLst )
	         {  
	        	 if(stance.equals(temp.name))
	        	 {
	        		  count++ ; 
	        		  bfsVertices = graph.breadthFirstSearch(temp);
	 
	        		  for(Vertex<String> temp3 : bfsVertices)
	        		  {
	        			  bfsVertexObject = new JSONObject();
	        			  bfsVertexObject.put("Vertex_name",temp3.name);
	        			  bfsVertexObject.put("Vertex_type",temp3.type);
	        			  bfsVertexObject.put("Vertex_data",temp3.data);
	        			  bfsVertexObject.put("Vertex_note",temp3.note);
	        			  bfsVertexObject.put("Vertex_authority",temp3.authority);
	        			  bfsVertexObject.put("Vertex_trust",temp3.trust);
	        			  bfsVertexObject.put("Vertex_rating",temp3.rating);  
	        			  bfsVerticesMap.put("Stance"+count, bfsVertexObject);
	        		  }
	       
	        		 bfsVertices.clear();
	        		 
	        		 dfsVertices = graph.depthFirstSearch(temp);
	        		 
	        		 for(Vertex<String> temp4 : dfsVertices)
	        		  {
	        			  dfsVertexObject = new JSONObject();
	        			  dfsVertexObject.put(" ",temp4);
	        			  dfsVerticesMap.put("Stance"+count, dfsVertexObject);
	        		  }
	        		 
	        		 dfsVertices.clear();
	        		 
	        	 }
	         }
	  }
	  
	  System.out.println("Breadth first search:");
	  System.out.println(bfsVerticesMap);
	  
	  System.out.println("---------------------------------------------");
	  
	  System.out.println("Depth first search:");
	  System.out.println(dfsVerticesMap);
	  
	  System.out.println("---------------------------------------------");*/
	  
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
	  
	  //creating JSONObject and putting information into it
	  int subgraphVertex_yes = 1 ;
	  JSONObject subgraphvertexobj_yes ;
	  for(Vertex<String> temp : subgraph.getVertices())
	  {
		  subgraphvertexobj_yes = new JSONObject();
		  subgraphvertexobj_yes.put("Vertex_name",temp.name);
		  subgraphvertexobj_yes.put("Vertex_type",temp.type);
		  subgraphvertexobj_yes.put("Vertex_data",temp.data);
		  subgraphvertexobj_yes.put("Vertex_note",temp.note);
		  subgraphvertexobj_yes.put("Vertex_authority",temp.authority);
		  subgraphvertexobj_yes.put("Vertex_trust",temp.trust);
		  subgraphvertexobj_yes.put("Vertex_rating",temp.rating);  
		  subgraphVerticesMap_yes.put(subgraphVertex_yes++ , subgraphvertexobj_yes);
	  }
	  
	  //diplaying JSONObject
	  System.out.println(subgraphVerticesMap_yes);
	  out.println(subgraphVerticesMap_yes);
	  
	  System.out.println("--------------------------");
	  
	  System.out.println("The number of edges in graph is:" + subgraph.edgessize());
	  System.out.println("The information about edges");
	  
	  int subgraphEdge_yes = 1 ;
	  JSONObject subgraphedgeobj_yes ;
	  for(Edge<String> temp2 : subgraph.getEdges())
	  {
		  subgraphedgeobj_yes = new JSONObject();
		  subgraphedgeobj_yes.put("Edge_from",temp2.from);
		  subgraphedgeobj_yes.put("Edge_to",temp2.to);
		  subgraphedgeobj_yes.put("Edge_cost",temp2.cost);
		  subgraphedgeobj_yes.put("Edge_connection",temp2.connection); 
		  subgraphEdgesMap_yes.put(subgraphEdge_yes++ , subgraphedgeobj_yes);
	  }
	  
	  System.out.println(subgraphEdgesMap_yes);
	  out.println(subgraphEdgesMap_yes);
	  
	  System.out.println("---------------------------------------------");
	  
	  rs4.close();
	  
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
	  
	//creating JSONObject and putting information into it
	  int subgraphVertex_no = 1 ;
	  JSONObject subgraphvertexobj_no ;
	  for(Vertex<String> temp : subgraph.getVertices())
	  {
		  subgraphvertexobj_no = new JSONObject();
		  subgraphvertexobj_no.put("Vertex_name",temp.name);
		  subgraphvertexobj_no.put("Vertex_type",temp.type);
		  subgraphvertexobj_no.put("Vertex_data",temp.data);
		  subgraphvertexobj_no.put("Vertex_note",temp.note);
		  subgraphvertexobj_no.put("Vertex_authority",temp.authority);
		  subgraphvertexobj_no.put("Vertex_trust",temp.trust);
		  subgraphvertexobj_no.put("Vertex_rating",temp.rating);  
		  subgraphVerticesMap_no.put(subgraphVertex_no++ , subgraphvertexobj_no);
	  }
	  
	  //diplaying JSONObject
	  System.out.println(subgraphVerticesMap_no);
	  out.println(subgraphVerticesMap_no);
	  
	  System.out.println("--------------------------");
	  
	  System.out.println("The number of edges in graph is:" + subgraph.edgessize());
	  System.out.println("The information about edges");
	  
	  int subgraphEdge_no = 1 ;
	  JSONObject subgraphedgeobj_no ;
	  for(Edge<String> temp2 : subgraph.getEdges())
	  {
		  subgraphedgeobj_no = new JSONObject();
		  subgraphedgeobj_no.put("Edge_from",temp2.from);
		  subgraphedgeobj_no.put("Edge_to",temp2.to);
		  subgraphedgeobj_no.put("Edge_cost",temp2.cost);
		  subgraphedgeobj_no.put("Edge_connection",temp2.connection); 
		  subgraphEdgesMap_no.put(subgraphEdge_no++ , subgraphedgeobj_no);
	  }
	  
	  System.out.println(subgraphEdgesMap_no);
	  out.println(subgraphEdgesMap_no);
	  
	  System.out.println("---------------------------------------------");

	  rs5.close();
	  
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
}//end init
   
   
   
   public void destroy()
   {
       // do nothing.
   }
}//end 

