# perception-check

A clojure library to help find your "things" in text. Provide perception-check a "database" of "things" and optionally a set of "rules" on how "things" should be structured and it finds them in text you provide.

The original goal of this project was to try to algorithmically find street-based locations in tweets from food trucks (think 123 N State or Madison & Clark).

## Concepts

perception-check uses OpenNLP's parts-of-speech tagger and chunker to get a list of noun-phrases out of text. perception-check takes these noun-phrases a checks to see if any contain your "things". If they do, it tries to validate them further. In the end you should have a processed list of "things" associated with your text.

There are two basic knobs in perception-check a "things database" and a "rules database". 

In my testing the "things database" has been a plain-text file of valid street names that I downloaded from the City of Chicago's Open Data Portal. I suppose the interface for this could be a "things database" is "anything that responds to a contains? or member? function".

The other knob is defining a set of rules for completely valid "things". In my tests this has been trying to validate that the resulting noun-phrases are fully valid addresses. "James Madison" would pass the "things database" test of perception-check, but isn't a valid address. "123 W Madison" is a valid address. My end goal was to pin-point on a map these "street-based locations" so a final rule could be "can this be geocoded?". The result should be the text provided and the latitude/longitude associated with that text.

I'm sure something like this has already been done, but I'm having fun learning clojure and NLP so I don't care.

## Usage

TBD

For now I'm testing with:

    lein run -m perception_check.core "Tweet text"

## TODO

* Add ability to provide perception-check a configuration "database" to query against for "things"
* Add ability to provide perception-check "rules" for how your "things" should appear in text. For instance, if you're trying to find "street-based locations", rules might be "123 W [THING]" or "[THING] & [THING]"

## License

Copyright © 2012 Ryan Briones <ryan.briones@brionesandco.com>. Perception-check source distributed under the MIT License. See LICENSE for details.

OpenNLP models are Copyright © 2010, 2011 The Apache Software Foundation and are licensed under the Apache License Version 2.0.