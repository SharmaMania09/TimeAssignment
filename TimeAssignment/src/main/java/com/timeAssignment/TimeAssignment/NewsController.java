package com.timeAssignment.TimeAssignment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Controller
public class NewsController
{
    @RequestMapping("/")
    public String home()
    {
        // System.out.println("In News Controller Java ");
        return "Home";
    }

    @GetMapping("getTimeStories")
    @ResponseBody
    public String getTimeStories()
    {
        return getLatestNews();
    }

    public String getLatestNews()
    {
        String url = "https://time.com";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        ArrayList<Integer> startIndexes = getIndexes(result, "<li class=\"latest-stories__item\">");
        ArrayList<Integer> endIndexes = getIndexes(result, "<time class=\"latest-stories__item-timestamp\">");

        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();

        for(int i=0;i<startIndexes.size();i++)
        {
            String temp = result.substring(startIndexes.get(i), endIndexes.get(i));
            int linkStartIndexes = temp.indexOf("<a href=\"");
            int linkEndIndexes = temp.indexOf( "/\">");

            System.out.println(temp.substring(linkStartIndexes+9, linkEndIndexes));

            links.add("https://time.com"+temp.substring(linkStartIndexes+9, linkEndIndexes));

            int titleStartIndexes = temp.indexOf( "<h3 class=\"latest-stories__item-headline\">");
            int titleEndIndexes = temp.indexOf("</h3>");

            //System.out.println(temp.substring(titleStartIndexes+42, titleEndIndexes));

            title.add(temp.substring(titleStartIndexes+42, titleEndIndexes));
        }

        JSONArray response = new JSONArray();

        for(int i=0;i<links.size();i++)
        {
            JSONObject obj = new JSONObject();

            obj.put("title", title.get(i));
            obj.put("link", links.get(i));

            response.put(obj);
        }

        return response.toString();
    }

    public static ArrayList<Integer> getIndexes(String result, String str)
    {
        ArrayList<Integer> response = new ArrayList<>();

        int idx = result.indexOf(str);

        while (idx != -1)
        {
            response.add(idx);
            idx = result.indexOf(str, idx + 1);
        }

        return response;
    }
}
