IRoadTrip ReadME

About My Code:
-constructor in the beginning creates hashmaps and reads files 
-reading files sets values in hashmaps
-the graph that I use to find path is the bordersMap which contains the Country, <Neighbor, Distance>
*-If you print out the bordersMap you can see that the distances were set correctly, if distance did not exist it was set to -1
-Ran into issue regarding the names of the countries mismatching on input 
-Like United States was marked invalid but United States of America worked
-Ran out of time but I needed to code a method to deal with this issue by hard coding each case 
-such as converting UK in borders.txt to United Kingdom to get the key in state_name.txt and storing these in a hashmap
-Issue with findPath not correctly identifying where to go and what to print, kept saying path was invalid, couldn't find the exact issue in my logic
*-Tried to implement Dijkstra's algorithm to find shortest path

Summary:
Main issue was code didn't fully run when put together and I ran out of time to figure out why
I *'d the main looking points above because I think those were the important parts I tried to code
I added comments within my code in all caps to indicate places where I met errors based on running the code and what I thought caused them
I recommend trying the print statement for bordersMap because I think I was doing fine until that point, after that I was unable to use the 
graph and inputs together to find the right path. 
