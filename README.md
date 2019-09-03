# Cronos

Cronos is a partitioning tool for CSV formatted databases with temporal attributes.

## Download

Access the website [Cronos](https://raulvitorl.github.io) to download it

## Installation

Run the downloaded JAR file

```bash
java -jar Cronos.jar
```

## Usage

You need to have the temporal attribute in order, otherwise you won't be able to perform the partition

The you open the CSV file in Cronos, set the metrics: 



Unity Type : Month or Year.

Duration: How long each interval is going to have.

Interval: how long will be despised from one interval to the next.

Date Formats Accepted: Look at your file and set the date format the fits to your base on Cronos.  

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[GPL-3.0](https://www.gnu.org/licenses/gpl-3.0.pt-br.html)
