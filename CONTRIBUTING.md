## Getting Started

The following are required: The Java 8 jdk, and your IDE of choice.

After you make sure you have both of these things, fork this repository and download the source.
Once you have forked the repo and downloaded the source you can open as a maven project in your IDE of choice.

__ALL CHANGES MUST BE MADE TO THE BRANCH YOU WISH TO MERGE WITH__


## conventions

> Interfaces
>
> - Interface are name in the format of `IInterface` an iterface for users would be `IUser`
> - All edit/fetch methods should return a `CompletableFuture<typeOfInterface>`