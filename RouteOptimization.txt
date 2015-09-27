# Route_Optimizer_OLA
Route Optimisation of two routes A..B and C..D

Given two routes, algorithm solves the problem of optimizing the routes to yield a new route which has the least deviation and maximum overlapping with respect to the original route. Especially useful when solving car pooling problems.

Suppose, given two paths A..B and C..D.

These are all the possible permutations of a,b,c and d.


a b c d

a b d c                                                                                                          
a c b d                                                                                                          
a c d b                                                                                                         
a d b c                                                                                                        
a d c b                                                                                                        
b a c d                                                                                                        
b a d c                                                                                                   
b c a d                                                                                                         
b c d a                                                                                                      
b d a c                                                                                                        
b d c a                                                                                                     
c a b d                                                                                                         
c a d b                                                                                                        
c b a d                                                                                                       
c b d a                                                                                                         
c d a b                                                                                                      
c d b a                                                                                                      
d a b c                                                                                                         
d a c b                                                                                                          
d b a c                                                                                                        
d b c a                                                                                                         
d c a b                                                                                                      
d c b a     

Eliminating various paths based on certain constraints like follows:

1. B cannot come before A
2. D cannot come before C

Results in the following paths.

a b c d 

a c b d                  
a c d b                             
c a b d     
c a d b                  
c d a b

Out of which, again (a b c d) and (c d a b) have to be eliminated since they become two seperate trips.
The next 4 trips remaining are the only ones we are going to take into consideration for our calculations.
Overlapping and Deviation for these routes will be calculated as follows.

			OVERLAPPING AND DEVIATION

Overlapping: 

1. A->C->B->D: Overlapping would be distance from C to B.

	A----(C----B)----D

2. A->C->D->B: Overlapping would be distance from C to D.
	
	A----(C----D)----B

3. C->A->B->D: Overlapping would be distance from A to B.

	C----(A----B)----D        

4. C->A->D->B: Overlapping would be distance from A to D.
 
 	C----(A---D)----B

All the overlapping distances can be found out by results we have already obtained by the Google Distance API. Since, it gives distcance between each legs.


Deviation: 

For all the routes, the deviation will be calculated as:

		Total distance of the route - Difference between ((A and B) + (C and D)).

		Example : For Route 1 above, deviation will be
		Difference between Total distance of the route 1. ie. (A->C->B->D) - ((A and B) + (C and D)) 


Thus the most optimized route will be chosen based on following factors:
1. Maximum Overlapping and 
2. Minimum Deviation

					PLEASE NOTE
 **(I've given a higher priority to overlapping, which can be changed when needed.)**

							ALGORITHM FOR BEST ROUTE

INPUT: Two routes((A...B) and (C...D)) 
OUTPUT: One Route(i..j..k..l, which is one of the four routes mentioned above)

1. Make request to Google APIs, fetch distances for the 4 routes explained above.
2. Accordingly, calculate overlapping and deviation for the 4 routes.
3. Tabulate them. Search for the least deviation and output the route, along with it's overlapping and deviation.
4. If there are more, choose the one with the most overlapping. (Refer to the note above.)
Repeat the algo for three routes, if required.
