VEHICLE AND POLYGON

1. Polling the api is not a good Idea and that is the reason I have a Redis cache for a predefined time period.
This helps me to not load the server with lot of requests.
Considering scenarios like no request for period of time due to any reason, we are polling for no reason.
Also this architecture can be scaled/changed  instantly removing or increasing time period depends on scenarios.

2. Also I would love to have query parameter search in /vehicles API instead of
retrieving all the vehicles. Because I believe right now Single responsibility of microservices are not completely
followed. Right now in-memory searches are happening withing vehicles retrieved in my microservice.

3. Location Cache also present incase we plan to remove a location or change the information.

4. We do not allow to search for all polygons without a polygon json provided, since this result does not makes any sense.
Because we return all Vehicles data if no search value provided. And each of then contains which polygon it belongs to.

5. Search supports both ways.
	a. Search happens on Vehicles first (in-memory) based on vehicle attributes. Then finds matching GeoLocations based on the vehicle coordinates to fill up Polygon Ids.

	b. Stores already found vehicle and polygon ids in cache for later search.

	c. Later Search happens on GeoLocation database for requested polygons based on polygon values specified by user.
	The above cache will help to fill up vehicle information present inside each polygon.
	But the polygons with no vehicle info in cache needs to be fetched from database.
	Hence database $geoNear query is fired for each vehicle not present in cache to find matching polygons.
	(This is made efficient by specifying db to search in only some polygon ids matched in the above query)
