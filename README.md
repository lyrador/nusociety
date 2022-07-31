# NUSociety

![nusociety banner](https://user-images.githubusercontent.com/65401176/182033140-a9985718-06a3-4e6c-9e96-fe4e0e5f3094.png)


NUSociety is an all-in-one co-curricular activities (Society) portal for NUS students. It provides a one-stop platform for NUS students to find information about student societies and events, sign up for them, network and communicate within and across organisations and keep track of their involvement. It also has the ability to allow student leaders and staff to check for their members’ attendance and manage the activities and events.

Backend development is done with Jakarta EE. Front-end development is done with Jakarta Server Faces (JSF) for Server-side processing (Java stack) and Angular for Client-side processing (JavaScript/TypeScript stack). GlassFish for Jakarta EE is used for the application server.

For the front-end applications, the MVVM software architectural setup is used, while the common backend adopts component-based architecture and Service-oriented Architecture (SOA). The overall enterprise software system exhibits a multitier architecture with thin clients.
A relational database is used to store data processed by the project permanently through the use of Jakarta Persistence API object/relational mapping (ORM) technology.

NOTE: This repository contains the Jakarta Servlet Faces (JSF) Web Application portion of the project. Look at the Angular portion in this other repository: https://github.com/lyrador/nusociety-angular
