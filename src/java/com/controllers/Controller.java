
package com.controllers;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet(name = "controller", urlPatterns = {"/accueil"})
public class Controller extends HttpServlet {
    
    public static final int TAILLE_TAMPON = 10240;
    public static final String CHEMIN_FICHIERS = "C:/Users/DELL/Desktop/ajoutWebMapping/"; 
                            // endroit sur notre machine où les fichiers uploadés sont rangés

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JSONArray send = affichageCarte();
        request.setAttribute("layerGroup", send);
        this.getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // url pour poster la nouvelle couche
        String url = "http://localhost:8080/geoserver/rest/workspaces/Cameroun/datastores/cameroun_GisData/file.shp";
        
        Part part = request.getPart("shapefile");  // On récupère le champ du fichier
        String nomFichier = getNomFichier(part);   // On vérifie qu'on a bien reçu un fichier
        
        if (nomFichier != null && !nomFichier.isEmpty()) { // Si on a bien un fichier
            String nomChamp = part.getName();
            // Corrige un bug du fonctionnement d'Internet Explorer
             nomFichier = nomFichier.substring(nomFichier.lastIndexOf('/') + 1)
                    .substring(nomFichier.lastIndexOf('\\') + 1);
            ecrireFichier(part, nomFichier, CHEMIN_FICHIERS);  // On écrit définitivement le fichier sur le disque
        }
        
        String location = CHEMIN_FICHIERS + nomFichier;
//        jsonPostRequest(url,location);
        
        String RESTURL  = "http://localhost:8080/geoserver";
        String RESTUSER = "admin";
        String RESTPW   = "geoserver";
        
        //GeoServerRESTReader reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
        GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);
        
        File zipFile = new File(location);
        nomFichier = nomFichier.replace(".zip", "");
        boolean published = publisher.publishShp("Cameroun", "cameroun_GisData", nomFichier, zipFile, "EPSG:4326", "default_point");

        
        // Après l'upload du layer je modifie directement son srs à EPSG:4326 pour que l'affichage se passe sans bemol
//        String base = "http://localhost:8080/geoserver/rest/workspaces/Cameroun/datastores/cameroun_GisData/featuretypes/";
//        JSONObject featuresByLayout = jsonGetRequest(base+nomFichier);
//        try {
//            featuresByLayout.getJSONObject("featureType").remove("srs");
//            featuresByLayout.getJSONObject("featureType").put("srs", "EPSG:4326");
//        } catch (JSONException ex) {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
//        jsonPutFeatureRequest(base+nomFichier,featuresByLayout.toString());
        
        JSONArray send = affichageCarte();
        request.setAttribute("layerGroup", send);
        this.getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

    // Traitement affichage carte
    public JSONArray affichageCarte(){
        JSONObject test = new JSONObject();
        try {
            test.put("layers", "");
        } catch (JSONException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        String base = "http://localhost:8080/geoserver/rest/workspaces/Cameroun/datastores/cameroun_GisData/featuretypes/";
        JSONObject layerGroup = jsonGetRequest("http://localhost:8080/geoserver/rest/workspaces/Cameroun/layers");
        JSONArray send = new JSONArray(); // pour renvoyer juste un json contenant le tableau de layer
           
        if(layerGroup.toString().equals(test.toString())== false){
            
            try {
                JSONArray table = layerGroup
                        .getJSONObject("layers").getJSONArray("layer");

                for (int i = 0 ; i < table.length(); i++) {
                    JSONObject objI = table.getJSONObject(i);
                    String nameI = objI.getString("name");

                    JSONObject featuresByLayout = jsonGetRequest(base+nameI);
                    JSONObject table2_1 = null;
                    JSONArray table2_2 = null;
                
                    try {
                        table2_1= featuresByLayout
                            .getJSONObject("featureType")
                            .getJSONObject("attributes")
                            .getJSONObject("attribute");

                        table2_2= featuresByLayout
                            .getJSONObject("featureType")
                            .getJSONObject("attributes")
                            .getJSONArray("attribute");

                    } catch (Exception e) {
                    }
                
                    ArrayList<String> myNames = new ArrayList<>();

                    if(table2_2 != null){
                        for (int j = 0 ; j < table2_2.length(); j++) {
                            JSONObject objJ = table2_2.getJSONObject(j);
                            String nameJ = objJ.getString("name");
                            myNames.add(nameJ);
                        }
                    }
                    if(table2_1 != null){
                        String nameJ = table2_1.getString("name");
                        myNames.add(nameJ);
                    }
                    objI.put("attributes", myNames);
                }
                send = table;
            } catch (JSONException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return send; 
    }
    
    private void ecrireFichier( Part part, String nomFichier, String chemin ) throws IOException {
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            entree = new BufferedInputStream(part.getInputStream(), TAILLE_TAMPON);
            sortie = new BufferedOutputStream(new FileOutputStream(new File(chemin + nomFichier)), TAILLE_TAMPON);

            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur;
            while ((longueur = entree.read(tampon)) > 0) {
                sortie.write(tampon, 0, longueur);
            }
        } finally {
            try {
                sortie.close();
            } catch (IOException ignore) {
            }
            try {
                entree.close();
            } catch (IOException ignore) {
            }
        }
    }
    
    private static String getNomFichier( Part part ) {
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        return null;
    }   
    

    // Methodes pour appeler pour gerer les URLs
    
    public static void jsonPostRequest(String url, String parameters) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");

            String user_name="admin";
            String password="geoserver";

            String userCredentials = user_name+":"+password;
            String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestProperty("Content-Type", "application/zip");  
            
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();
            con.connect();
            int responseCode = con.getResponseCode();
            System.out.println("Sending 'PUT' request to URL : " + url);
            System.out.println("Put parameters : " + parameters);
            System.out.println("Response Code : " + responseCode);
            
            } catch (IOException e) {
             System.out.println(e);
           }
    }
    
    public static JSONObject jsonGetRequest(String url) {
        JSONObject jsonObj = null;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            
            String user_name="admin";
            String password="geoserver";

            String userCredentials = user_name+":"+password;
            String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            
            int responseCode = con.getResponseCode();
            System.out.println("Sending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
             response.append(inputLine);
            }
            in.close();
            jsonObj = new JSONObject(response.toString());
            } catch (IOException | JSONException e) {
             System.out.println(e);
           }
        return jsonObj;
    }
   
    
    public static void jsonPutFeatureRequest(String url, String parameters) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");

            String user_name="admin";
            String password="geoserver";

            String userCredentials = user_name+":"+password;
            String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
            con.setRequestProperty ("Authorization", basicAuth);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();
            con.connect();
            int responseCode = con.getResponseCode();
            System.out.println("Sending 'PUT' request to URL : " + url);
            System.out.println("Put parameters : " + parameters);
            System.out.println("Response Code : " + responseCode);
            
            } catch (IOException e) {
             System.out.println(e);
           }
    }
    
}
