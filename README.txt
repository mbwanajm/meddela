

  |-------------|
  |    \   /    |
  |     \_/     |
  |   meddela   |
  |-------------|


Imagine you have written some Banking software which uses an SQL database 
as a data store. The software is installed at bank ABCD which has 4 branches. 
Each branch is run by its own manager. A branch manager has a number of 
tellers under him. You have installed the software and it has run for about 5 months. 

On a review meeting, one branch manager has asked out if the software can
send him an email everyday at 12:00pm summarizing the number of new clients
each teller in his branch has serviced something like:


Hello Michael, 
Here is a summary of your new clients:
mbwana: 3 new Accounts
Boniface: 10 new Accounts
Noel: 6 new Accounts

regards
ABCD Bank


This email has to go to each branch manager giving him the summary of their branch 
and it should only go only if new clients are registered, if there are no new clients 
then it should not be sent out.

So you sit down think... mh now I have to take the source code of the application, 
add this functionality, recompile and go back to install it at the bank! what a 
lot for such a small functionality!

This is where meddela steps in. With meddela you don’t have to do all that. 
All you need to do is install meddela, write a few SQL queries and it will 
take care of the rest. Put simply meddela is a notification system. It generates
and sends out notifications. The next questions will probably be for what, 
from where and through what?

For what? →  for anything.
From what? → SQL database OR Groovy Script
Through what? → anything (SMS, tweets, email e.t.c.)

SQL database is the primary source of information on which meddela works on.
Once given information on how to connect to the SQL and a couple of SQL queries
meddela can check the state of the SQL database and see if there is a need to send
out a notification, if it finds out there is one, it sends out a notification 
getting the content and receiver of the notification from the SQL database. 
This implies that the only knowledge that is required of the normal meddela user is how
to write SQL queries.

For the bit advanced user, meddela goes further by adding the ability of using groovy 
scripts to trigger and provide information for your notifications. With the power 
of groovy scripts meddela can send out notifications from technically any source.

meddela delegates the delivering of notifications to plug-ins. Various plug-ins for 
meddela transports can be created e.g. (SMS, email, tweets). Furthermore you can write
your own transport plug-in and upload it to meddela and meddela will be able to send 
out notifications via that transport.

There are two ways to use meddela. The first is via the web interface. This means 
that you can use your browser to access, configure and view reports for meddela from
anywhere via its web user interface.

The second way to use meddela is to use it as a Java library in your projects.
meddela provides a simple API that can allow you to do all and even more activities 
than what the web user interface allows you.














