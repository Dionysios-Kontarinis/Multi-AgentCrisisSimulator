Multi-AgentCrisisSimulator
============================

This is a project which consists in simulating a humanitarian crisis, after the hit of an earthquake.
We assume a region where buildings have collapsed, trapping people under the debris.
The physical system is modelled as a multi-agent system, as we can identify different types of agents (actors) in such a scenario:

1. A **Command Center (CC)** which has an overview of the geographical impact of the earthquake, and it coordinates
the efforts of firefighters and doctors.
2. A set of **Firefighters (FF)** whose task is follow the commands of the CC and go to areas with debris, searching for survivors.
3. A set of **Doctors (D)** whose task is to follow the commands of the CC and treat heavily injured survivors on the spot.
4. A set of **Victims (V)** who are trapped under the debris, and suffer injuries of various gravities. 

For the implementation, we have used the **_JADE_** platform (http://jade.tilab.com/) which is a well-known java platform
for implementing multi-agent systems.

These simulations can help us determine good "strategies" for the Command Center, which have been
shown to maximize the (average) number of victims saved. 
Although this project is small, it is easily scalable:
the agents' behaviours could be easily turned more elaborate, and we could add new types of agents
(e.g. Search-Dogs which speed up the searching process).

This project was a collaborative work with **_Julien Marçais_**.