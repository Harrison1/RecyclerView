# RecyclerView
RecyclerView that mimics a listview with the option to put the list into a card view. The data is pulled from Reddit's open API parsing their JSON data.

You can follow my tutorial series on how to make recyclerviews here
https://www.youtube.com/watch?v=8ePqYGMxdSY

Below is the picture of the final app in cardview
![Alt text](/screenshots/card1.PNG?raw=true)

Below are two pictures of the app in listview. The second picture shows the progressdialog while loading new content for the endless scroller feature. 
![Alt text](/screenshots/list1.PNG?raw=true)
![Alt text](/screenshots/list2.PNG?raw=true)

As is, the code displays the content similar to a listview. To convert the display to card view format uncomment the designated sections in the list_row xml layout file and the FragmentList file. Below are pictures of what to uncomment. 
![Alt text](/screenshots/xml1.PNG?raw=true)
![Alt text](/screenshots/xml2.PNG?raw=true)
![Alt text](/screenshots/decoration.PNG?raw=true)
