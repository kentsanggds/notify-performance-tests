# Warning
This will SMS people by default. Be very careful. There are options with TEST API keys, Simulated services, Provider API Keys etc that
will negate the risk of blowing a large some of money.


# notifications-performance-tests
Performance tests suite for GOV.UK Notify.

## Setting Up

Based on [Gatling](http://gatling.io/)

## Running tests

Tests are configured by the _simulation.conf_ file in the root of the project. This is in [HOCON](https://github.com/typesafehub/config) format.

An example configuration is documented in the example file:

```
simulation_example.conf
```

#### Optional Configuration

The configuration is optional depending on the test simulation chosen at runtime (the user will be presented with a list to choose from).
The following examples demonstrate how to configure the tests for those simulations where there is are optional items.

#### Testing API Config file example Email / SMS:

```
notify {
  basic {
    base-url="http://the-url-to-the-api/"
    min-wait=200
    max-wait=750
    users=1
    ramp-seconds=100
    unit=times
    repeat=60
    app="api"
  }
  services=[
    {
      name="This is my service name"
      api-key="API-KEY"
      service-id="SERVICE-ID"
      templates {
        sms="SMS-TEMPLATE-ID"
        email="EMAIL-TEMPLATE-ID"
        personalisation_key="personalisation_key"
      }
    }
  ]
}
```

#### Testing API Config file example Letter:
This example assumes that the template used has a added personalisation of subject i.e. in the template ((subject)) and content i.e. in the template ((content)).

```
notify {
  basic {
    base-url="http://the-url-to-the-api/"
    min-wait=200
    max-wait=750
    users=1
    ramp-seconds=100
    unit=times
    repeat=60
    app="api"
  }
  services=[
    {
      name="This is my service name"
      api-key="API-KEY"
      service-id="SERVICE-ID"
      templates {
        letter="TEMPLATE-ID"
        address-line-1="Address Line 1"
        address-line-2="Address Line 2"
        postcode="Post code"
        subject="this is a subject"
	    content="this is the content"
    }
  ]
}
```

#### Testing Template Preview Config file example:

```
notify {
  basic {
    base-url="http://the-url-to-the-api/"
    min-wait=200
    max-wait=750
    users=1
    ramp-seconds=100
    unit=times
    repeat=60
    app="template-preview"
  }
  services=[
    {
      auth-token="template preview auth token"
    }
  ]
}
```

#### Config file details

##### basic
- base-url -- URL of the service (url including protocol) (required)
- min-wait -- min wait between calls (milliseconds) (required)
- max-wait -- max wait between calls (milliseconds) (required)
- users -- number of concurrent users (integer) (required)
- ramp-seconds -- period of time over which to inject the users at a constant rate. Set to 1 introduces all users at once.
- unit -- iteration unit, either times (number of calls per user) or seconds (total time of the test run) (required)
- repeat -- repetition either seconds or times depending on the unit (required)
- app -- the app which is being tested, currently either 'api' or 'template-preview'
##### services
- services -- list of services

##### each api service
- name -- service name, used only for documentation in this file (text) (required)
- api-key -- api key from the service (UUID) (required)
- service-id -- service id (UUID) (required)

##### each template-preview service
- template-preview -- bearer token used by the template-preview app (text) (required)

##### Email / SMS template, 1 block per service (not required for template preview)
- templates -- templates
- sms -- sms template ID (UUID) (required)
- email -- email template ID (UUID) (required)
- personalisation_key -- personalisation key on the template e.g. ((name)) this is substituted for a random string (text) (required)


##### Letter template, 1 block per service (not required for template preview)
- templates -- templates
- letter -- The template ID of the letter to send (UUID) (required)
- address-line-1 -- The content for the first line of the address in the contact block (text) (required)
- address-line-2 -- The content for the second line of the address in the contact block (text) (required)
- postcode -- The postcode for the contact block (text) (required)
- subject --  The subject line for the letter (text) (required)
- content -- The content of the letter (text) (required)


## User agent
The Notify API will graph API usage by User agent. If the user agent begins NOTIFY, then the API expects there to be a backslash "/" followed by a version number of the form 1.0.0.
Changing this version number *could* be used to identify a test run in Grafana as it will get it's own graph.

This is configured in:

```
NotifyHttpConf.scala
```

## Dead numbers
Dead numbers are numbers used for film and tv productions. [https://www.ofcom.org.uk/phones-telecoms-and-internet/information-for-industry/numbering/numbers-for-drama](https://www.ofcom.org.uk/phones-telecoms-and-internet/information-for-industry/numbering/numbers-for-drama)

These will not be delivered by the SMS providers, but will allow us to test an end to end journey including the SMS provider without hitting a real phone. They will cost money though.

A thousand are provided here for convenience. The tests will select one at random from the file:

```
user-files/data/simulated_numbers.csv
```

To run against a different set of numbers, replace this file with the numbers you want to test against.


## Simulated emails

AWS provides simulated email address to test the various flows. The list is provided here. They can be used and will not be delivered to users but can test the end to end flow.

[http://docs.aws.amazon.com/ses/latest/DeveloperGuide/mailbox-simulator.html](http://docs.aws.amazon.com/ses/latest/DeveloperGuide/mailbox-simulator.html)

```
user-files/data/simulated_emails.csv
```

The default file includes some error scenarios. Edit this file to create the scenarios you may want to test.


## Running the tests:

Run the helper script:

```
./scripts/run-gatling.sh
```

This will execute the tests against your simulation.conf.

### Test Simulations

There are currently 7 simulations configured:
- Fetch notifications (API)
- Send SMS and EMAIL (API)
- Send Email (API)
- Send Letter (API)
- Send SMS (API)
- Fetch PDF Preview (Template Preview)
- Fetch PNG Preview (Template Preview)

These are defined in the file:

```
NotifySimulations.scala
```

Each simulation is a combination of one or more scenarios, which are defined in:

```
NotifyScenarios.scala
```

## Test reports (GUI)

Tests output reports to the console, and additionally create an HTML report in

```
results/notifysimulation-xxx/index.html
```

where the xxx part is replaced by the test run.
