# Jetty Server Builder

## Purpose

Make it simple (as in one line of code) to configure an embedded Jetty server for either inclusion in a standalone app or for testing.

## The Reason

I started this project because Jetty, while easy to embed, should be even easier. Like many Java developers, I use Jetty to run webapps for testing, and I'm looking to expand the way I use Jetty in tests, so I want a really simple way to create a Jetty server. I [asked on StackOverflow](http://stackoverflow.com/q/15311544/839646), and there doesn't seem to be anything else like this out there.

## The Goal

This project aims to provide a simple, [fluent interface](http://en.wikipedia.org/wiki/Fluent_interface) for building Jetty [Servers](http://download.eclipse.org/jetty/stable-8/apidocs/org/eclipse/jetty/server/Server.html) tailored for specific needs. My initial aim is to support Spring MVC and Jersey, since those are what I need it for. I intend to design the project in such a way that if Jetty can be configured to do something, then a module can be written here that will provide a fluent interface for it.
