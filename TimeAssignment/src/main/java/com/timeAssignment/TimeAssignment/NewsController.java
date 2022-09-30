package com.timeAssignment.TimeAssignment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

// This function will act as a controller which is used to handle request and response for api or action call
@Controller
public class NewsController
{
    // JSP home page which contains a button to fetch top 6 latest news from https://www.time.com
    @RequestMapping("/")
    public String home()
    {
        // System.out.println("In News Controller Java ");
        return "Home";
    }

    // This is a get api call which will retrieves the top 6 latest news
    // Return type is string which contains the response
    @GetMapping("getTimeStories")
    @ResponseBody
    public String getTimeStories()
    {
        return getLatestNews();
    }

    // Helper function
    // Return: String which contains the response
    public String getLatestNews()
    {
        String url = "https://time.com";
        
        // Rest Template which will help in getting the html content from https://www.time.com
        RestTemplate restTemplate = new RestTemplate();
        // Result contains the html content which will then be parsed to get title and link
        String result = restTemplate.getForObject(url, String.class);

        // Matching the string in result to get the start index and end index because the content lies in between them
        // getIndexes is a helper function which is used to retrieve the matching string index 
        ArrayList<Integer> startIndexes = getIndexes(result, "<li class=\"latest-stories__item\">");
        ArrayList<Integer> endIndexes = getIndexes(result, "<time class=\"latest-stories__item-timestamp\">");

        // These arraylist contains the links and title which will then be constructed and send as a response
        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();

        // We can use either start index or end index as the size will remain same which is 6 in our case
        for(int i=0;i<startIndexes.size();i++)
        {
            // First creating the string in which we need to find link and title
            String temp = result.substring(startIndexes.get(i), endIndexes.get(i));
            
            // To find the link from the temp string by finding the start index and end index of href
            int linkStartIndexes = temp.indexOf("<a href=\"");
            int linkEndIndexes = temp.indexOf( "/\">");

            // Used for debugging purpose
            // System.out.println(temp.substring(linkStartIndexes+9, linkEndIndexes));

            // Adding the link in the links arraylist
            links.add("https://time.com"+temp.substring(linkStartIndexes+9, linkEndIndexes));

            // To find the title from the temp string by finding the start index and end index of latest-stories
            int titleStartIndexes = temp.indexOf( "<h3 class=\"latest-stories__item-headline\">");
            int titleEndIndexes = temp.indexOf("</h3>");

            // Used for debugging purpose
            // System.out.println(temp.substring(titleStartIndexes+42, titleEndIndexes));

            // Adding the title in the title arraylist
            title.add(temp.substring(titleStartIndexes+42, titleEndIndexes));
        }

        // Response containing the final result
        JSONArray response = new JSONArray();

        // To construct the response in json object which will be finally added in json array
        for(int i=0;i<links.size();i++)
        {
            JSONObject obj = new JSONObject();

            obj.put("title", title.get(i));
            obj.put("link", links.get(i));

            response.put(obj);
        }
        // Final Response
        return response.toString();
    }

    // Helper function which is used to get indexes from result string by finding str string
    // params - result: String from which we want to find the index
    // params - str: String which needs to be find in the result string
    // Return: ArrayList which contains the index
    public static ArrayList<Integer> getIndexes(String result, String str)
    {
        ArrayList<Integer> response = new ArrayList<>();

        int idx = result.indexOf(str);

        // We keep on finding the index until it is not equal to -1 and the result is being persisted in the arraylist
        while (idx != -1)
        {
            response.add(idx);
            idx = result.indexOf(str, idx + 1);
        }
        //
        return response;
    }
}
