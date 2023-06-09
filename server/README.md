# Swim Server

Swim unifies the traditionally disparate roles of database, message broker, job manager, and application server, into a few simple constructs.

*Read this in other languages: [简体中文](README.zh-cn.md)*

## Web Agents

Swim implements a general purpose distributed object model. The "objects" in this model are **Web Agents**.

[Creating a class](http://github.com/swimos/tutorial/tree/master/server/src/main/java/swim/tutorial/UnitAgent.java#L13) that extends `swim.api.agent.AbstractAgent` defines a *template* for Web Agents (though not a useful one until we add some [lanes](#lanes)).

Visit the [documentation](https://swimos.org/concepts/agents/) for further details about Web Agents.

## Lanes

If Web Agents are objects, then **lanes** serve as the "fields" of those objects. Lanes come in many flavors, e.g. value lanes, map lanes, and command lanes.

Continuing our analogy, *lane callback* functions serve as the "methods" of Web Agents.

Each lane type defines a set of overridable (default no-op) lifecycle callbacks. For example, [sending a command message](#sending-data-do-swim) to any command lane will trigger its [`onCommand` callback](http://github.com/swimos/tutorial/tree/master/server/src/main/java/swim/tutorial/UnitAgent.java#L51-L54). On the other hand, [setting a value lane](http://github.com/swimos/tutorial/tree/master/server/src/main/java/swim/tutorial/UnitAgent.java#L53) will trigger its `willSet` callback, then update its value, then trigger its [`didSet` callback](http://github.com/swimos/tutorial/tree/master/server/src/main/java/swim/tutorial/UnitAgent.java#L40-L47).

Visit the [documentation](https://swimos.org/concepts/lanes/) for further details about lanes.

## Standing a Swim Server

A Swim server is loaded with a **plane**, which provides the runtime for Web Agents and routes messages to the correct lanes.

A plane must [declare a `SwimRoute` field](http://github.com/swimos/tutorial/tree/master/server/src/main/java/swim/tutorial/TutorialPlane.java#L13-L14) for each Web Agent type.

Use the `ServerLoader` utility class to [load the default kernel modules](http://github.com/swimos/tutorial/tree/master/server/src/main/java/swim/tutorial/TutorialPlane.java#L17).

Visit the [documentation](https://swimos.org/concepts) for further details about these concepts.

## Populating a Swim Server With Your Data

Every Swim server can be written to and read from by external processes using the Swim API. The simplest way to utilize this API is to use a **Swim client** instance to [send commands to command lanes](http://github.com/swimos/tutorial/blob/master/server/src/main/java/swim/tutorial/DataSource.java#L42).

Visit the [documentation](https://swimos.org/concepts) for further details about these concepts.

## Subscribing to Swim Server Data

Swim client instances use Swim **links** to pull data from a Swim lanes. Like their corresponding lanes, links have overridable callback functions that can be used to [populate UIs](http://github.com/swimos/tutorial/tree/master/ui/index.html#L116-L141).

Visit the [documentation](https://swimos.org/concepts/links/) for further details about links.
