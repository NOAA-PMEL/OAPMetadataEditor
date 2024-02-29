package oap

import grails.converters.JSON
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

class OracleController {

    FundingService fundingService
    VocabularyService vocabularyService

//    def gcmd_variables = [ // maybe? maybe CF?
//            "Earth Science > Atmosphere > Atmospheric Temperature > Surface Temperature > Dew Point Temperature > Dewpoint Depression",
//            "Earth Science > Atmosphere > Atmospheric Temperature > Surface Temperature > Maximum/Minimum Temperature > 24 Hour Maximum Temperature",
//            "Earth Science > Atmosphere > Atmospheric Temperature > Surface Temperature > Maximum/Minimum Temperature > 24 Hour Minimum Temperature",
//            "Earth Science > Atmosphere > Atmospheric Temperature > Surface Temperature > Maximum/Minimum Temperature > 6 Hour Maximum Temperature",
//            "Earth Science > Atmosphere > Atmospheric Temperature > Surface Temperature > Maximum/Minimum Temperature > 6 Hour Minimum Temperature",
//            "Earth Science > Atmosphere > Atmospheric Temperature > Upper Air Temperature > Dew Point Temperature > Dew Point Depression",
//            "Earth Science > Atmosphere > Atmospheric Temperature > Upper Air Temperature > Vertical Profiles > Dry Adiabatic Lapse Rate",
//            "Earth Science > Atmosphere > Atmospheric Temperature > Upper Air Temperature > Vertical Profiles > Environmental Lapse Rate",
//            "Earth Science > Atmosphere > Atmospheric Temperature > Upper Air Temperature > Vertical Profiles > Inversion Height",
//            "Earth Science > Atmosphere > Atmospheric Temperature > Upper Air Temperature > Vertical Profiles > Saturated Adiabatic Lapse Rate",
//            "Earth Science > Atmosphere > Atmospheric Water Vapor > Water Vapor Indicators > Humidity > Absolute Humidity",
//            "Earth Science > Atmosphere > Atmospheric Water Vapor > Water Vapor Indicators > Humidity > Humidity Mixing Ratio",
//            "Earth Science > Atmosphere > Atmospheric Water Vapor > Water Vapor Indicators > Humidity > Relative Humidity",
//            "Earth Science > Atmosphere > Atmospheric Water Vapor > Water Vapor Indicators > Humidity > Saturation Specific Humidity",
//            "Earth Science > Atmosphere > Atmospheric Water Vapor > Water Vapor Indicators > Humidity > Specific Humidity",
//            "Earth Science > Atmosphere > Atmospheric Water Vapor > Water Vapor Processes > Evapotranspiration > Effective Evapotranspiration",
//            "Earth Science > Atmosphere > Atmospheric Water Vapor > Water Vapor Processes > Evapotranspiration > Potential Evapotranspiration",
//            "Earth Science > Atmosphere > Atmospheric Winds > Local Winds > Orographic Winds > Anabatic Winds",
//            "Earth Science > Atmosphere > Atmospheric Winds > Local Winds > Orographic Winds > Bora Winds",
//            "Earth Science > Atmosphere > Atmospheric Winds > Local Winds > Orographic Winds > Foehn Winds",
//            "Earth Science > Atmosphere > Atmospheric Winds > Local Winds > Orographic Winds > Katabatic Winds",
//            "Earth Science > Atmosphere > Atmospheric Winds > Local Winds > Orographic Winds > Mountain Breezes",
//            "Earth Science > Atmosphere > Atmospheric Winds > Local Winds > Orographic Winds > Valley Breezes",
//            "Earth Science > Atmosphere > Atmospheric Winds > Wind Dynamics > Vorticity > Potential Vorticity",
//            "Earth Science > Atmosphere > Atmospheric Winds > Wind Dynamics > Vorticity > Vorticity Advection",
//            "Earth Science > Atmosphere > Atmospheric Winds > Wind Dynamics > Wind Shear > Horizontal Wind Shear",
//            "Earth Science > Atmosphere > Atmospheric Winds > Wind Dynamics > Wind Shear > Vertical Wind Shear",
//            "Earth Science > Atmosphere > Clouds > Cloud Dynamics > Moisture Flux > Downward Moisture Flux",
//            "Earth Science > Atmosphere > Clouds > Cloud Dynamics > Moisture Flux > Upward Moisture Flux",
//            "Earth Science > Atmosphere > Clouds > Cloud Dynamics > Vortex Street > Karman Vortex Street",
//            "Earth Science > Atmosphere > Clouds > Cloud Microphysics > Droplet Growth > Accretion",
//            "Earth Science > Atmosphere > Clouds > Cloud Microphysics > Droplet Growth > Accretion",
//            "Earth Science > Atmosphere > Clouds > Cloud Microphysics > Droplet Growth > Aggregation",
//            "Earth Science > Atmosphere > Clouds > Cloud Microphysics > Droplet Growth > Coalescence",
//            "Earth Science > Atmosphere > Clouds > Cloud Microphysics > Sedimentation > Sedimentation Rate",
//            "Earth Science > Atmosphere > Clouds > Convective Clouds/Systems (Observed/Analyzed) > Cumulonimbus > Cumulonimbus Calvus",
//            "Earth Science > Atmosphere > Clouds > Convective Clouds/Systems (Observed/Analyzed) > Cumulonimbus > Cumulonimbus Capillatus",
//            "Earth Science > Atmosphere > Clouds > Convective Clouds/Systems (Observed/Analyzed) > Cumulonimbus > Cumulonimbus Capillatus",
//            "Earth Science > Atmosphere > Clouds > Convective Clouds/Systems (Observed/Analyzed) > Cumulonimbus > Pyrocumulonimbus",
//            "Earth Science > Atmosphere > Clouds > Convective Clouds/Systems (Observed/Analyzed) > Cumulus > Cumulus Castellanus",
//            "Earth Science > Atmosphere > Clouds > Convective Clouds/Systems (Observed/Analyzed) > Cumulus > Cumulus Congestus",
//            "Earth Science > Atmosphere > Clouds > Convective Clouds/Systems (Observed/Analyzed) > Cumulus > Cumulus Humilis",
//            "Earth Science > Atmosphere > Clouds > Convective Clouds/Systems (Observed/Analyzed) > Cumulus > Cumulus Mediocris",
//            "Earth Science > Atmosphere > Clouds > Convective Clouds/Systems (Observed/Analyzed) > Cumulus > Pyrocumulus",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/High-Level Clouds (Observed/Analyzed) > Cirrus/Systems > Cirrus Cloud Systems",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/High-Level Clouds (Observed/Analyzed) > Cirrus/Systems > Cirrus Kelvin-Helmholtz Colombiah",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Fog > Advection Fog",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Fog > Frontal Fog",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Fog > Ice Fog",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Fog > Radiation Fog",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Fog > Steam Fog",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Fog > Upslope Fog",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Marine Stratocumulus",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Cumiliformis",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Cumiliformis",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Cumiliformis",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Cumiliformis",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Cumiliformis",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Undulatas",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Undulatas",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Undulatas",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Undulatas",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Low Level Clouds (Observed/Analyzed) > Stratocumulus > Stratocumulus Undulatas",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Mid-Level Clouds (Observed/Analyzed) > Altocumulus > Altocumulus Castellanus",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Mid-Level Clouds (Observed/Analyzed) > Altocumulus > Altocumulus Lenticularis",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Mid-Level Clouds (Observed/Analyzed) > Altocumulus > Altocumulus Lenticularis",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Mid-Level Clouds (Observed/Analyzed) > Altocumulus > Altocumulus Undulatus",
//            "Earth Science > Atmosphere > Clouds > Tropospheric/Mid-Level Clouds (Observed/Analyzed) > Altostratus > Altostratus Undulatus",
//            "Earth Science > Atmosphere > Precipitation > Liquid Precipitation > Drizzle > Freezing Drizzle",
//            "Earth Science > Atmosphere > Precipitation > Liquid Precipitation > Rain > Acid Rain",
//            "Earth Science > Atmosphere > Precipitation > Liquid Precipitation > Rain > Freezing Rain",
//            "Earth Science > Atmosphere > Precipitation > Solid Precipitation > Ice Pellets > Sleet",
//            "Earth Science > Atmosphere > Precipitation > Solid Precipitation > Ice Pellets > Small Hail",
//            "Earth Science > Atmosphere > Precipitation > Solid Precipitation > Snow > Snow Grains",
//            "Earth Science > Atmosphere > Precipitation > Solid Precipitation > Snow > Snow Pellets",
//            "Earth Science > Atmosphere > Weather Events > Subtropical Cyclones > Subtropical Depression > Subtropical Depression Track",
//            "Earth Science > Atmosphere > Weather Events > Subtropical Cyclones > Subtropical Storm > Subtropical Storm Motion",
//            "Earth Science > Atmosphere > Weather Events > Subtropical Cyclones > Subtropical Storm > Subtropical Storm Track",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Accumulated Cyclone Energy > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Accumulated Cyclone Energy > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Accumulated Cyclone Energy > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Accumulated Cyclone Energy > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Accumulated Cyclone Energy > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Landfall Intensity > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Landfall Intensity > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Landfall Intensity > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Landfall Intensity > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Landfall Intensity > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum 1-Minute Sustained Wind > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum 1-Minute Sustained Wind > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum 1-Minute Sustained Wind > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum 1-Minute Sustained Wind > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum 1-Minute Sustained Wind > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Surface Wind > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Surface Wind > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Surface Wind > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Surface Wind > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Surface Wind > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Wind Gust > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Wind Gust > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Wind Gust > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Wind Gust > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Maximum Wind Gust > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Minimum Central Pressure > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Minimum Central Pressure > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Minimum Central Pressure > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Minimum Central Pressure > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Minimum Central Pressure > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Peak Intensity > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Peak Intensity > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Peak Intensity > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Peak Intensity > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Peak Intensity > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Saffir-Simpson Scale At Landfall (Category 1) > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Saffir-Simpson Scale At Landfall (Category 2) > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Saffir-Simpson Scale At Landfall (Category 3) > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Saffir-Simpson Scale At Landfall (Category 4) > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Saffir-Simpson Scale At Landfall (Category 5) > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Force Wind Extent > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Force Wind Extent > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Force Wind Extent > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Force Wind Extent > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Force Wind Extent > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Motion > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Motion > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Motion > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Motion > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Motion > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Radius > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Radius > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Radius > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Radius > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Radius > Typhoons (Western N. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Track > Cyclones (Sw Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Track > Hurricanes  (N. Atlantic/E. Pacific)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Track > Severe Cyclonic Storms (N. Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Track > Severe Tropical Cyclones (Sw Pacific/Se Indian)",
//            "Earth Science > Atmosphere > Weather Events > Tropical Cyclones > Tropical Cyclone Track > Typhoons (Western N. Pacific)",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Chelicerates > Arachnids",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Crustaceans > Amphipods",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Crustaceans > Barnacles",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Crustaceans > Copepods",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Crustaceans > Decapods",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Crustaceans > Euphausiids (Krill)",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Crustaceans > Isopods",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Crustaceans > Mysids",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Crustaceans > Ostracods",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Hexapods > Entognatha",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Hexapods > Insects",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Myriapods > Centipedes",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Arthropods > Myriapods > Millipedes",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Cnidarians > Anthozoans/Hexacorals > Hard Or Stony Corals",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Cnidarians > Anthozoans/Hexacorals > Sea Anemones",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Cnidarians > Anthozoans/Octocorals > Sea Fans/Sea Whips",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Cnidarians > Anthozoans/Octocorals > Sea Pens",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Cnidarians > Anthozoans/Octocorals > Soft Corals",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Mollusks > Bivalves > Clams",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Mollusks > Bivalves > Mussels",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Mollusks > Bivalves > Oysters",
//            "Earth Science > Biological Classification > Animals/Invertebrates > Mollusks > Cephalopods > Squids",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Anchovies/Herrings",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Catfishes/Minnows",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Cods/Haddocks",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Flounders",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Needlefishes",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Perch-Like Fishes",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Perch-Like Fishes",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Pupfishes",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Salmons/Trouts",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Fish > Ray-Finned Fishes > Sturgeons/Paddlefishes",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Carnivores > Bears",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Carnivores > Dogs/Foxes/Wolves",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Carnivores > Martens/Weasels/Wolverines",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Carnivores > Otters",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Carnivores > Seals/Sea Lions/Walruses",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Cetaceans > Baleen Whales",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Cetaceans > Toothed Whales",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Even-Toed Ungulates > Cattle/Sheep",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Even-Toed Ungulates > Deer/Moose",
//            "Earth Science > Biological Classification > Animals/Vertebrates > Mammals > Even-Toed Ungulates > Hogs/Pigs",
//            "Earth Science > Biological Classification > Plants > Angiosperms (Flowering Plants) > Monocots > Seagrass",
//            "Earth Science > Biological Classification > Plants > Microalgae > Haptophytes > Coccolithophores",
//            "Earth Science > Biological Classification > Protists > Flagellates > Haptophytes > Coccolithophores",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Agricultural Lands > Cropland",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Agricultural Lands > Forest Plantation",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Agricultural Lands > Pasture",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Agricultural Lands > Rangeland",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Resource Development Site > Minning/Drilling Site",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Resource Development Site > Solar Farm",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Resource Development Site > Water Impoundment",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Resource Development Site > Wind Farm",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Urban Lands > Canal",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Urban Lands > Garden",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Urban Lands > Park",
//            "Earth Science > Biosphere > Ecosystems > Anthropogenic/Human Influenced Ecosystems > Urban Lands > Roadside",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Plankton > Phytoplankton",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Plankton > Zooplankton",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Wetlands > Estuarine Wetlands",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Wetlands > Lacustrine Wetlands",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Wetlands > Marine",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Wetlands > Marshes",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Wetlands > Palustrine Wetlands",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Wetlands > Peatlands",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Wetlands > Riparian Wetlands",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Wetlands > Swamps",
//            "Earth Science > Biosphere > Ecosystems > Aquatic Ecosystems > Wetlands > Vernal Pool",
//            "Earth Science > Biosphere > Ecosystems > Freshwater Ecosystems > Lake/Pond > Montane Lake",
//            "Earth Science > Biosphere > Ecosystems > Freshwater Ecosystems > Lake/Pond > Saline Lakes",
//            "Earth Science > Biosphere > Ecosystems > Freshwater Ecosystems > Rivers/Stream > Ephemeral Stream",
//            "Earth Science > Biosphere > Ecosystems > Freshwater Ecosystems > Rivers/Stream > Headwater Stream",
//            "Earth Science > Biosphere > Ecosystems > Freshwater Ecosystems > Rivers/Stream > Intermittent Stream",
//            "Earth Science > Biosphere > Ecosystems > Freshwater Ecosystems > Rivers/Stream > Perennial Stream/River",
//            "Earth Science > Biosphere > Ecosystems > Freshwater Ecosystems > Rivers/Stream > River Delta",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Abyssal > Cold Seep",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Abyssal > Hydrothermal Vent",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Coastal > Beaches",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Coastal > Dunes",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Coastal > Kelp Forest",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Coastal > Lagoon",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Coastal > Mangrove Swamp",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Coastal > Mudflat",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Coastal > Rocky Intertidal",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Coastal > Salt Marsh",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Coastal > Sav/Sea Grass Bed",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Estuary > Brackish Marsh",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Estuary > Mangrove Swamp",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Estuary > Mudflat",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Estuary > Sav/Sea Grass Bed",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Pelagic > Neritic Zone",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Pelagic > Oceanic Zone",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Reef > Coral Reef",
//            "Earth Science > Biosphere > Ecosystems > Marine Ecosystems > Reef > Oyster Reef",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Alpine/Tundra > Alpine Tundra",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Alpine/Tundra > Arctic Tundra",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Alpine/Tundra > Subalpine",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Deserts > Desert Scrub",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Forests > Boreal Forest/Tiaga",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Forests > Temperate Coniferous Forest",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Forests > Temperate Deciduous Forest",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Forests > Temperate Mixed Forest",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Forests > Temperate Rainforest",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Forests > Tropical Rainforest",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Grasslands > Montane Grassland",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Grasslands > Savanna",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Shrubland/Scrub > Chaparral",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Shrubland/Scrub > Montane Shrubland",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Wetlands > Estuarine Wetlands",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Wetlands > Lacustrine Wetlands",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Wetlands > Marine",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Wetlands > Marshes",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Wetlands > Palustrine Wetlands",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Wetlands > Peatlands",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Wetlands > Riparian Wetlands",
//            "Earth Science > Biosphere > Ecosystems > Terrestrial Ecosystems > Wetlands > Swamps",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Humidity Indices > Temperature-Humidity Index > Thi",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Humidity Indices > Water Vapor Transport Index > Wtvi",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Ocean Upwelling Indices > Ocean Coastal Upwelling Index > Cui",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > Caribbean Index > Car",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > Central Tropical Pacific Sst > Nino 4 Index",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > East Central Tropical Pacific Sst > Nino 3.4 Index",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > Extreme Eastern Tropical Pacific Sst > Nino 1+2 Index",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > North Tropical Atlantic Index > Nta",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > Oceanic Nino Index > Oni",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > Trans-Nino Index > Tni",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > Tropical North Atlantic Index > Tna",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > Tropical South Atlantic Index > Tsa",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Sea Surface Temperature Indices > Western Hemisphere Warm Pool > Whwp",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Antarctic Oscillation > Aao",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Arctic Oscillation > Ao",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Atlantic Meridional Mode > Amm",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Atlantic Multidecadal Oscillation Long Version > Amo",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Bivariate Enso Timeseries Index > Best",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > East Atlantic Jet Pattern > Ea-Jet",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > East Atlantic Pattern > Eatl",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Eastern Atlantic Western Russia Pattern > Eatl/Wrus",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Eastern Pacific Oscillation > Ep/Np",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > El Nino Southern Oscillation (Enso) > Enso",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Madden-Julian Oscillation > Mjo",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Multivariate Enso Index > Mei",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > North Atlantic Oscillation > Nao",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > North Pacific Oscillation > Npo",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > North Pacific Pattern > Np",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Northern Oscillation Index > Noi",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Pacific Decadal Oscillation > Pdo",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Pacific/North American (Pna) Pattern > Pna",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Pacific Transition Index > Pt",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Quasi-Biennial Oscillation > Qbo",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Southern Oscillation Index > Soi",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > Tropical/Northern Hemisphere Pattern > Tnh",
//            "Earth Science > Climate Indicators > Atmospheric/Ocean Indicators > Teleconnections > West Pacific Index > Wp",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Biological Records > Tree Rings > Isotopic Analysis",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Biological Records > Tree Rings > Isotopic Analysis",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Biological Records > Tree Rings > Isotopic Analysis",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Biological Records > Tree Rings > Isotopic Analysis",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Ice Core Records > Isotopes > Argon Isotopes",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Ice Core Records > Isotopes > Nitrogen Isotopes",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Ice Core Records > Isotopes > Oxygen Isotopes",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Ice Core Records > Particulate Matter > Microparticle Concentration",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Land Records > Fire History > Charcoal Sediment",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Land Records > Fire History > Fire Scar Date",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Land Records > Sediments > Sediment Thickness",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Ocean/Lake Records > Sediments > Sediment Thickness",
//            "Earth Science > Climate Indicators > Paleoclimate Indicators > Paleoclimate Reconstructions > Sediments > Sediment Thickness",
//            "Earth Science > Cryosphere > Glaciers/Ice Sheets > Ice Sheets > Ice Sheet Measurements > Rifts",
//            "Earth Science > Human Dimensions > Public Health > Diseases/Epidemics > Epidemiology > Tele-Epidemiology",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Crescentic (Barchan/Transverse) Dune",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Dome Dune",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Longitudinal/Linear Dune",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Parabolic Dune",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Star Dune",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Processes > Abrasion > Ventifacts",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Processes > Abrasion > Yardangs",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Processes > Sediment Transport > Loess",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Processes > Sediment Transport > Monadnock",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Processes > Sedimentation > Sediment Chemistry",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Processes > Sedimentation > Sediment Composition",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Aeolian Processes > Sedimentation > Stratigraphic Sequence",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Apron Reef",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Atoll Reef",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Bank Reef",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Barrier Reef",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Fringing Reef",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Patch Reef",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Ribbon Reef",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Table Reef",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Crescentic (Barchan/Transverse)",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Dome Dune",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Longitudinal/Linear Dune",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Parabolic Dune",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Star Dune",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Processes > Sedimentation > Sediment Chemistry",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Processes > Sedimentation > Sediment Composition",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Coastal Processes > Sedimentation > Stratigraphic Sequence",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Fluvial Landforms > Valley > V Shaped Valley",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Fluvial Processes > Sedimentation > Sediment Chemistry",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Fluvial Processes > Sedimentation > Sediment Composition",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Fluvial Processes > Sedimentation > Stratigraphic Sequence",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Landforms > Crevasses > Longitudinal Crevasses",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Landforms > Crevasses > Marginal Crevasses",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Landforms > Crevasses > Transverse Crevasses",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Lateral Moraine",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Medial Moraine",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Recessional Moraine",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Ribbed/Rogan Moraine",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Terminal Moraine",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Processes > Freeze/Thaw > Basal Ice Freezing",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Processes > Sedimentation > Sediment Chemistry",
//            "Earth Science > Land Surface > Geomorphic Landforms/Processes > Glacial Processes > Sedimentation > Stratigraphic Sequence",
//            "Earth Science > Land Surface > Land Use/Land Cover > Land Use/Land Cover Classification > Vegetation Index > Normalized Difference Vegetation Index (Ndvi)",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Crescentic (Barchan/Transverse) Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Dome Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Longitudinal/Linear Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Parabolic Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Landforms > Dunes > Star Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Processes > Abrasion > Ventifacts",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Processes > Abrasion > Yardangs",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Processes > Sediment Transport > Loess",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Processes > Sediment Transport > Monadnock",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Processes > Sedimentation > Sediment Chemistry",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Processes > Sedimentation > Sediment Composition",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Aeolian Processes > Sedimentation > Stratigraphic Sequence",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Apron Reef",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Atoll Reef",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Bank Reef",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Barrier Reef",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Fringing Reef",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Patch Reef",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Ribbon Reef",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Coral Reefs > Table Reef",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Crescentic (Barchan/Transverse) Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Dome Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Longitudinal/Linear Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Parabolic Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Landforms > Dunes > Star Dune",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Processes > Sedimentation > Sediment Chemistry",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Processes > Sedimentation > Sediment Composition",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Processes > Sedimentation > Straitraphic Sequence",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Coastal Processes > Wave Erosion > Degradation",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Fluvial Landforms > Valley > V Shapped Valley",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Fluvial Processes > Sedimentation > Sediment Chemistry",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Fluvial Processes > Sedimentation > Sediment Composition",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Fluvial Processes > Sedimentation > Straitigraphic Sequence",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Landforms > Crevasses > Longitudinal Crevasses",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Landforms > Crevasses > Marginal Crevasses",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Landforms > Crevasses > Transverse Crevasses",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Lateral Moraine",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Medial Moraine",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Recessional Moraine",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Ribbed/Rogan Moraine",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Landforms > Moraines > Terminal Moraine",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Processes > Freeze/Thaw > Basal Ice Freezing",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Processes > Sedimentation > Sediment Chemistry",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Glacial Processes > Sedimentation > Stratigraphic Sequence",
//            "Earth Science > Solid Earth > Geomorphic Landforms/Processes > Karst Processes > Karst Hydrology > Subsurface Drainage",
//            "Earth Science > Solid Earth > Geothermal Dynamics > Geothermal Temperature > Temperature Gradient > Temperature Gradient Rate",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Cleavage",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Color",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Composition/Texture",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Electrical",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Hardness",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Luminescence",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Luster",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Reflection",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Specific Gravity",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Gas Hydrates > Gas Hydrates Physical/Optical Properties > Stability",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Cleavage",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Color",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Composition/Texture",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Electrical",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Hardness",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Luminescence",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Luster",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Reflection",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Specific Gravity",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Igneous Rocks > Igneous Rock Physical/Optical Properties > Stability",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Cleavage",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Color",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Composition/Structure",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Electrical",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Hardness",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Luminescence",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Luster",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Reflection",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Specific Gravity",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metals > Metals Physical/Optical Properties > Stability",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Cleavage",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Color",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Composition/Texture",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Electricial",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Hardness",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Luminescence",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Luster",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Reflection",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Specific Gravity",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Metamorphic Rocks > Metamorphic Rock Physical/Optical Properties > Stability",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Cleavage",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Color",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Composition/Structure",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Electrical",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Hardness",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Luminescence",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Luster",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Reflection",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Specific Gravity",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Physical/Optical Properties > Stability",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Meteorites > Meteorite Vertical/Geogrpahic Distribution > Luster",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Cleavage",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Color",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Composition/Texture",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Electrical",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Hardness",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Luminescence",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Luster",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Reflection",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Specific Gravity",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Minerals > Mineral Physical/Optical Properties > Stability",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Cleavage",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Color",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Composition/Texture",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Electrical",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Hardness",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Luminescence",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Luster",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Reflection",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Specific Gravity",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Non-Metallic Minerals > Non-Metallic Mineral Physical/Optical Properties > Stability",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Cleavage",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Color",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Composition/Texture",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Electrical",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Hardness",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Luminescence",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Luster",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Reflection",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Specific Gravity",
//            "Earth Science > Solid Earth > Rocks/Minerals/Crystals > Sedimentary Rocks > Sedimentary Rock Physical/Optical Properties > Stability",
//            "Earth Science > Solid Earth > Tectonics > Earthquakes > Seismic Profile > Seismic Body Waves",
//            "Earth Science > Solid Earth > Tectonics > Earthquakes > Seismic Profile > Seismic Surface Waves",
//            "Earth Science > Solid Earth > Tectonics > Plate Tectonics > Crustal Motion > Crustal Motion Direction",
//            "Earth Science > Solid Earth > Tectonics > Plate Tectonics > Crustal Motion > Crustal Motion Rate",
//            "Earth Science > Solid Earth > Tectonics > Plate Tectonics > Fault Movement > Fault Movement Direction",
//            "Earth Science > Solid Earth > Tectonics > Plate Tectonics > Fault Movement > Fault Movement Rate",
//            "Earth Science > Solid Earth > Tectonics > Plate Tectonics > Isostatic Rebound > Rebound Direction",
//            "Earth Science > Solid Earth > Tectonics > Plate Tectonics > Isostatic Rebound > Rebound Rate",
//            "Earth Science > Solid Earth > Tectonics > Plate Tectonics > Lithospheric Plate Motion > Plate Motion Direction",
//            "Earth Science > Solid Earth > Tectonics > Plate Tectonics > Lithospheric Plate Motion > Plate Motion Rate",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Ash/Dust Composition",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Ash/Dust Dispersion",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Gas/Aerosol Composition",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Gas/Aerosol Dispersion",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Lava Composition/Texture",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Lava Speed/Flow",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Magma Composition/Texture",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Magma Speed/Flow",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Pyroclastic Partical Size Distribution",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Pyroclastics Composition/Texture",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Volcanic Explosivity",
//            "Earth Science > Solid Earth > Tectonics > Volcanic Activity > Eruption Dynamics > Volcanic Gases",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Features > Water Table > Water Table Depth",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Aquifer Recharge > Aquifer Depth",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Aquifer Recharge > Recharge Amount",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Aquifer Recharge > Recharge Frequency",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Discharge > Discharge Amount",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Discharge > Discharge Rate",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Dispersion > Dispersion Frequency",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Dispersion > Dispersion Rate",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Drainage > Drainage Amount",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Drainage > Drainage Direction",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Infiltration > Infiltration Amount",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Infiltration > Infiltration Frequency",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Infiltration > Infiltration Rate",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Land Subsidence > Subsidence Amount",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Land Subsidence > Subsidence Rate",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Percolation > Percolation Amount",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Percolation > Percolation Rate",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Saltwater Intrusion > Intrusion Amount",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Saltwater Intrusion > Intrusion Rate",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Subsurface Flow > Average Flow",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Subsurface Flow > Flow Velocity",
//            "Earth Science > Terrestrial Hydrosphere > Ground Water > Ground Water Processes/Measurements > Subsurface Flow > Peak Flow",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Aquifer Recharge > Aquifer Depth",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Aquifer Recharge > Recharge Amount",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Aquifer Recharge > Recharge Frequency",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Discharge/Flow > Average Flow",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Discharge/Flow > Base Flow",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Discharge/Flow > Flow Velocity",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Discharge/Flow > Peak Flow",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Drainage > Drainage Amount",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Drainage > Drainage Direction",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Floods > Flood Frequency",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Floods > Flood Levels",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Inundation > Inundation Amount",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Inundation > Inundation Frequency",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Inundation > Inundation Level",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Runoff > Runoff Rate",
//            "Earth Science > Terrestrial Hydrosphere > Surface Water > Surface Water Processes/Measurements > Runoff > Total Runoff"
//    ]
    def getAllSuggestions(Collection<String> vocabulary) {
        List<NceiSuggestion> allSuggestions = new ArrayList<NceiSuggestion>()
        for (String item : vocabulary) {
            NceiSuggestion v = new NceiSuggestion()
            v.setSuggestion(item)
            allSuggestions.add(v)
        }
        return allSuggestions
    }
    def getSuggestions(Set<String> vocabulary) {
        String query
        String method = request.getMethod()
        if ( method.equals("POST")) {
            JSONObject queryJSON = request.getJSON()
            SuggestQuery vq = new SuggestQuery(queryJSON)
            query = vq.getQuery()
        } else {
            query = getQueryFromString(request.getQueryString())
        }
        return getQuerySuggestions(query, vocabulary)
    }

    def getQueryFromString(String queryString) {
        def query = ""
        if (queryString) {
            def q = queryString.toLowerCase()
            if ( q.contains("q=")) {
                int qidx = q.indexOf("q=")
                int end = q.indexOf('&',qidx) > 0 ?
                        q.indexOf('&',qidx) :
                        q.length()
                query = q.substring(qidx+"q=".length(), end)
            } else
            if ( q.contains("query=")) {
                int qidx = q.indexOf("query=")
                int end = q.indexOf('&',qidx) > 0 ?
                            q.indexOf('&',qidx) :
                            q.length()
                query = q.substring(qidx+"query=".length(), end)
            } else {
                query = queryString
            }
        }
        return query as JSON
    }

    def getQuerySuggestions(String query, Set<String> vocabulary) {
        List<NceiSuggestion> suggestions
        if (query) {
            suggestions = new ArrayList<NceiSuggestion>()
            query = query.trim().toLowerCase()
            for (String item : vocabulary) {
                if (item.toLowerCase().contains(query)) {
                    NceiSuggestion v = new NceiSuggestion()
                    v.setSuggestion(item)
                    suggestions.add(v)
                }
            }
        } else {
            suggestions = getAllSuggestions(vocabulary)
        }
        render suggestions as JSON
    }
    def variable() {
        Collection<String> variables = vocabularyService.getNceiVariables()
        return getSuggestions(variables)
    }
    def instrument() {
        Collection<String> instruments = vocabularyService.getNceiInstruments()
        return getSuggestions(instruments)
    }
    def observationType () {
        Collection<String> observationTypes = vocabularyService.getNceiObservationTypes()
        return getSuggestions(observationTypes)
    }
    def platform() {
        Collection<String> platforms = vocabularyService.getNceiPlatforms()
        return getSuggestions(platforms)
    }
    def institution() {
        Collection<String> institutions = vocabularyService.getNceiInstitutions()
        return getSuggestions(institutions)
    }
    def funding() {
        Collection<String> fundings = fundingService.fundingSources()
        return getSuggestions(fundings)
    }

    def _funding() {
        def queryJSON = request.JSON
        SuggestQuery vq = new SuggestQuery(queryJSON)
        String query = vq.getQuery()
        if ( query ) { query = query.trim().toLowerCase() }
        def oap_fundings = fundingService.fundingSources()
        List<NceiSuggestion> suggestions = new ArrayList<NceiSuggestion>()
//        if (query) {
            for (String grantNum : oap_fundings) {
                if ( ! query || grantNum.toLowerCase().contains(query)) {
                    NceiSuggestion v = new NceiSuggestion()
                    v.setSuggestion(grantNum)
                    suggestions.add(v)
                }
            }
//        }
        render suggestions as JSON
    }

     static def countryJsonString = "[{\"name\":\"Afghanistan\",\"alpha-3\":\"AFG\",\"country-code\":\"004\"},{\"name\":\"Åland Islands\",\"alpha-3\":\"ALA\",\"country-code\":\"248\"},{\"name\":\"Albania\",\"alpha-3\":\"ALB\",\"country-code\":\"008\"},{\"name\":\"Algeria\",\"alpha-3\":\"DZA\",\"country-code\":\"012\"},{\"name\":\"American Samoa\",\"alpha-3\":\"ASM\",\"country-code\":\"016\"},{\"name\":\"Andorra\",\"alpha-3\":\"AND\",\"country-code\":\"020\"},{\"name\":\"Angola\",\"alpha-3\":\"AGO\",\"country-code\":\"024\"},{\"name\":\"Anguilla\",\"alpha-3\":\"AIA\",\"country-code\":\"660\"},{\"name\":\"Antarctica\",\"alpha-3\":\"ATA\",\"country-code\":\"010\"},{\"name\":\"Antigua and Barbuda\",\"alpha-3\":\"ATG\",\"country-code\":\"028\"},{\"name\":\"Argentina\",\"alpha-3\":\"ARG\",\"country-code\":\"032\"},{\"name\":\"Armenia\",\"alpha-3\":\"ARM\",\"country-code\":\"051\"},{\"name\":\"Aruba\",\"alpha-3\":\"ABW\",\"country-code\":\"533\"},{\"name\":\"Australia\",\"alpha-3\":\"AUS\",\"country-code\":\"036\"},{\"name\":\"Austria\",\"alpha-3\":\"AUT\",\"country-code\":\"040\"},{\"name\":\"Azerbaijan\",\"alpha-3\":\"AZE\",\"country-code\":\"031\"},{\"name\":\"Bahamas\",\"alpha-3\":\"BHS\",\"country-code\":\"044\"},{\"name\":\"Bahrain\",\"alpha-3\":\"BHR\",\"country-code\":\"048\"},{\"name\":\"Bangladesh\",\"alpha-3\":\"BGD\",\"country-code\":\"050\"},{\"name\":\"Barbados\",\"alpha-3\":\"BRB\",\"country-code\":\"052\"},{\"name\":\"Belarus\",\"alpha-3\":\"BLR\",\"country-code\":\"112\"},{\"name\":\"Belgium\",\"alpha-3\":\"BEL\",\"country-code\":\"056\"},{\"name\":\"Belize\",\"alpha-3\":\"BLZ\",\"country-code\":\"084\"},{\"name\":\"Benin\",\"alpha-3\":\"BEN\",\"country-code\":\"204\"},{\"name\":\"Bermuda\",\"alpha-3\":\"BMU\",\"country-code\":\"060\"},{\"name\":\"Bhutan\",\"alpha-3\":\"BTN\",\"country-code\":\"064\"},{\"name\":\"Bolivia (Plurinational State of)\",\"alpha-3\":\"BOL\",\"country-code\":\"068\"},{\"name\":\"Bonaire, Sint Eustatius and Saba\",\"alpha-3\":\"BES\",\"country-code\":\"535\"},{\"name\":\"Bosnia and Herzegovina\",\"alpha-3\":\"BIH\",\"country-code\":\"070\"},{\"name\":\"Botswana\",\"alpha-3\":\"BWA\",\"country-code\":\"072\"},{\"name\":\"Bouvet Island\",\"alpha-3\":\"BVT\",\"country-code\":\"074\"},{\"name\":\"Brazil\",\"alpha-3\":\"BRA\",\"country-code\":\"076\"},{\"name\":\"British Indian Ocean Territory\",\"alpha-3\":\"IOT\",\"country-code\":\"086\"},{\"name\":\"Brunei Darussalam\",\"alpha-3\":\"BRN\",\"country-code\":\"096\"},{\"name\":\"Bulgaria\",\"alpha-3\":\"BGR\",\"country-code\":\"100\"},{\"name\":\"Burkina Faso\",\"alpha-3\":\"BFA\",\"country-code\":\"854\"},{\"name\":\"Burundi\",\"alpha-3\":\"BDI\",\"country-code\":\"108\"},{\"name\":\"Cambodia\",\"alpha-3\":\"KHM\",\"country-code\":\"116\"},{\"name\":\"Cameroon\",\"alpha-3\":\"CMR\",\"country-code\":\"120\"},{\"name\":\"Canada\",\"alpha-3\":\"CAN\",\"country-code\":\"124\"},{\"name\":\"Cabo Verde\",\"alpha-3\":\"CPV\",\"country-code\":\"132\"},{\"name\":\"Cayman Islands\",\"alpha-3\":\"CYM\",\"country-code\":\"136\"},{\"name\":\"Central African Republic\",\"alpha-3\":\"CAF\",\"country-code\":\"140\"},{\"name\":\"Chad\",\"alpha-3\":\"TCD\",\"country-code\":\"148\"},{\"name\":\"Chile\",\"alpha-3\":\"CHL\",\"country-code\":\"152\"},{\"name\":\"China\",\"alpha-3\":\"CHN\",\"country-code\":\"156\"},{\"name\":\"Christmas Island\",\"alpha-3\":\"CXR\",\"country-code\":\"162\"},{\"name\":\"Cocos (Keeling) Islands\",\"alpha-3\":\"CCK\",\"country-code\":\"166\"},{\"name\":\"Colombia\",\"alpha-3\":\"COL\",\"country-code\":\"170\"},{\"name\":\"Comoros\",\"alpha-3\":\"COM\",\"country-code\":\"174\"},{\"name\":\"Congo\",\"alpha-3\":\"COG\",\"country-code\":\"178\"},{\"name\":\"Congo (Democratic Republic of the)\",\"alpha-3\":\"COD\",\"country-code\":\"180\"},{\"name\":\"Cook Islands\",\"alpha-3\":\"COK\",\"country-code\":\"184\"},{\"name\":\"Costa Rica\",\"alpha-3\":\"CRI\",\"country-code\":\"188\"},{\"name\":\"Côte d'Ivoire\",\"alpha-3\":\"CIV\",\"country-code\":\"384\"},{\"name\":\"Croatia\",\"alpha-3\":\"HRV\",\"country-code\":\"191\"},{\"name\":\"Cuba\",\"alpha-3\":\"CUB\",\"country-code\":\"192\"},{\"name\":\"Curaçao\",\"alpha-3\":\"CUW\",\"country-code\":\"531\"},{\"name\":\"Cyprus\",\"alpha-3\":\"CYP\",\"country-code\":\"196\"},{\"name\":\"Czech Republic\",\"alpha-3\":\"CZE\",\"country-code\":\"203\"},{\"name\":\"Denmark\",\"alpha-3\":\"DNK\",\"country-code\":\"208\"},{\"name\":\"Djibouti\",\"alpha-3\":\"DJI\",\"country-code\":\"262\"},{\"name\":\"Dominica\",\"alpha-3\":\"DMA\",\"country-code\":\"212\"},{\"name\":\"Dominican Republic\",\"alpha-3\":\"DOM\",\"country-code\":\"214\"},{\"name\":\"Ecuador\",\"alpha-3\":\"ECU\",\"country-code\":\"218\"},{\"name\":\"Egypt\",\"alpha-3\":\"EGY\",\"country-code\":\"818\"},{\"name\":\"El Salvador\",\"alpha-3\":\"SLV\",\"country-code\":\"222\"},{\"name\":\"Equatorial Guinea\",\"alpha-3\":\"GNQ\",\"country-code\":\"226\"},{\"name\":\"Eritrea\",\"alpha-3\":\"ERI\",\"country-code\":\"232\"},{\"name\":\"Estonia\",\"alpha-3\":\"EST\",\"country-code\":\"233\"},{\"name\":\"Ethiopia\",\"alpha-3\":\"ETH\",\"country-code\":\"231\"},{\"name\":\"Falkland Islands (Malvinas)\",\"alpha-3\":\"FLK\",\"country-code\":\"238\"},{\"name\":\"Faroe Islands\",\"alpha-3\":\"FRO\",\"country-code\":\"234\"},{\"name\":\"Fiji\",\"alpha-3\":\"FJI\",\"country-code\":\"242\"},{\"name\":\"Finland\",\"alpha-3\":\"FIN\",\"country-code\":\"246\"},{\"name\":\"France\",\"alpha-3\":\"FRA\",\"country-code\":\"250\"},{\"name\":\"French Guiana\",\"alpha-3\":\"GUF\",\"country-code\":\"254\"},{\"name\":\"French Polynesia\",\"alpha-3\":\"PYF\",\"country-code\":\"258\"},{\"name\":\"French Southern Territories\",\"alpha-3\":\"ATF\",\"country-code\":\"260\"},{\"name\":\"Gabon\",\"alpha-3\":\"GAB\",\"country-code\":\"266\"},{\"name\":\"Gambia\",\"alpha-3\":\"GMB\",\"country-code\":\"270\"},{\"name\":\"Georgia\",\"alpha-3\":\"GEO\",\"country-code\":\"268\"},{\"name\":\"Germany\",\"alpha-3\":\"DEU\",\"country-code\":\"276\"},{\"name\":\"Ghana\",\"alpha-3\":\"GHA\",\"country-code\":\"288\"},{\"name\":\"Gibraltar\",\"alpha-3\":\"GIB\",\"country-code\":\"292\"},{\"name\":\"Greece\",\"alpha-3\":\"GRC\",\"country-code\":\"300\"},{\"name\":\"Greenland\",\"alpha-3\":\"GRL\",\"country-code\":\"304\"},{\"name\":\"Grenada\",\"alpha-3\":\"GRD\",\"country-code\":\"308\"},{\"name\":\"Guadeloupe\",\"alpha-3\":\"GLP\",\"country-code\":\"312\"},{\"name\":\"Guam\",\"alpha-3\":\"GUM\",\"country-code\":\"316\"},{\"name\":\"Guatemala\",\"alpha-3\":\"GTM\",\"country-code\":\"320\"},{\"name\":\"Guernsey\",\"alpha-3\":\"GGY\",\"country-code\":\"831\"},{\"name\":\"Guinea\",\"alpha-3\":\"GIN\",\"country-code\":\"324\"},{\"name\":\"Guinea-Bissau\",\"alpha-3\":\"GNB\",\"country-code\":\"624\"},{\"name\":\"Guyana\",\"alpha-3\":\"GUY\",\"country-code\":\"328\"},{\"name\":\"Haiti\",\"alpha-3\":\"HTI\",\"country-code\":\"332\"},{\"name\":\"Heard Island and McDonald Islands\",\"alpha-3\":\"HMD\",\"country-code\":\"334\"},{\"name\":\"Holy See\",\"alpha-3\":\"VAT\",\"country-code\":\"336\"},{\"name\":\"Honduras\",\"alpha-3\":\"HND\",\"country-code\":\"340\"},{\"name\":\"Hong Kong\",\"alpha-3\":\"HKG\",\"country-code\":\"344\"},{\"name\":\"Hungary\",\"alpha-3\":\"HUN\",\"country-code\":\"348\"},{\"name\":\"Iceland\",\"alpha-3\":\"ISL\",\"country-code\":\"352\"},{\"name\":\"India\",\"alpha-3\":\"IND\",\"country-code\":\"356\"},{\"name\":\"Indonesia\",\"alpha-3\":\"IDN\",\"country-code\":\"360\"},{\"name\":\"Iran (Islamic Republic of)\",\"alpha-3\":\"IRN\",\"country-code\":\"364\"},{\"name\":\"Iraq\",\"alpha-3\":\"IRQ\",\"country-code\":\"368\"},{\"name\":\"Ireland\",\"alpha-3\":\"IRL\",\"country-code\":\"372\"},{\"name\":\"Isle of Man\",\"alpha-3\":\"IMN\",\"country-code\":\"833\"},{\"name\":\"Israel\",\"alpha-3\":\"ISR\",\"country-code\":\"376\"},{\"name\":\"Italy\",\"alpha-3\":\"ITA\",\"country-code\":\"380\"},{\"name\":\"Jamaica\",\"alpha-3\":\"JAM\",\"country-code\":\"388\"},{\"name\":\"Japan\",\"alpha-3\":\"JPN\",\"country-code\":\"392\"},{\"name\":\"Jersey\",\"alpha-3\":\"JEY\",\"country-code\":\"832\"},{\"name\":\"Jordan\",\"alpha-3\":\"JOR\",\"country-code\":\"400\"},{\"name\":\"Kazakhstan\",\"alpha-3\":\"KAZ\",\"country-code\":\"398\"},{\"name\":\"Kenya\",\"alpha-3\":\"KEN\",\"country-code\":\"404\"},{\"name\":\"Kiribati\",\"alpha-3\":\"KIR\",\"country-code\":\"296\"},{\"name\":\"Korea (Democratic People's Republic of)\",\"alpha-3\":\"PRK\",\"country-code\":\"408\"},{\"name\":\"Korea (Republic of)\",\"alpha-3\":\"KOR\",\"country-code\":\"410\"},{\"name\":\"Kuwait\",\"alpha-3\":\"KWT\",\"country-code\":\"414\"},{\"name\":\"Kyrgyzstan\",\"alpha-3\":\"KGZ\",\"country-code\":\"417\"},{\"name\":\"Lao People's Democratic Republic\",\"alpha-3\":\"LAO\",\"country-code\":\"418\"},{\"name\":\"Latvia\",\"alpha-3\":\"LVA\",\"country-code\":\"428\"},{\"name\":\"Lebanon\",\"alpha-3\":\"LBN\",\"country-code\":\"422\"},{\"name\":\"Lesotho\",\"alpha-3\":\"LSO\",\"country-code\":\"426\"},{\"name\":\"Liberia\",\"alpha-3\":\"LBR\",\"country-code\":\"430\"},{\"name\":\"Libya\",\"alpha-3\":\"LBY\",\"country-code\":\"434\"},{\"name\":\"Liechtenstein\",\"alpha-3\":\"LIE\",\"country-code\":\"438\"},{\"name\":\"Lithuania\",\"alpha-3\":\"LTU\",\"country-code\":\"440\"},{\"name\":\"Luxembourg\",\"alpha-3\":\"LUX\",\"country-code\":\"442\"},{\"name\":\"Macao\",\"alpha-3\":\"MAC\",\"country-code\":\"446\"},{\"name\":\"Macedonia (the former Yugoslav Republic of)\",\"alpha-3\":\"MKD\",\"country-code\":\"807\"},{\"name\":\"Madagascar\",\"alpha-3\":\"MDG\",\"country-code\":\"450\"},{\"name\":\"Malawi\",\"alpha-3\":\"MWI\",\"country-code\":\"454\"},{\"name\":\"Malaysia\",\"alpha-3\":\"MYS\",\"country-code\":\"458\"},{\"name\":\"Maldives\",\"alpha-3\":\"MDV\",\"country-code\":\"462\"},{\"name\":\"Mali\",\"alpha-3\":\"MLI\",\"country-code\":\"466\"},{\"name\":\"Malta\",\"alpha-3\":\"MLT\",\"country-code\":\"470\"},{\"name\":\"Marshall Islands\",\"alpha-3\":\"MHL\",\"country-code\":\"584\"},{\"name\":\"Martinique\",\"alpha-3\":\"MTQ\",\"country-code\":\"474\"},{\"name\":\"Mauritania\",\"alpha-3\":\"MRT\",\"country-code\":\"478\"},{\"name\":\"Mauritius\",\"alpha-3\":\"MUS\",\"country-code\":\"480\"},{\"name\":\"Mayotte\",\"alpha-3\":\"MYT\",\"country-code\":\"175\"},{\"name\":\"Mexico\",\"alpha-3\":\"MEX\",\"country-code\":\"484\"},{\"name\":\"Micronesia (Federated States of)\",\"alpha-3\":\"FSM\",\"country-code\":\"583\"},{\"name\":\"Moldova (Republic of)\",\"alpha-3\":\"MDA\",\"country-code\":\"498\"},{\"name\":\"Monaco\",\"alpha-3\":\"MCO\",\"country-code\":\"492\"},{\"name\":\"Mongolia\",\"alpha-3\":\"MNG\",\"country-code\":\"496\"},{\"name\":\"Montenegro\",\"alpha-3\":\"MNE\",\"country-code\":\"499\"},{\"name\":\"Montserrat\",\"alpha-3\":\"MSR\",\"country-code\":\"500\"},{\"name\":\"Morocco\",\"alpha-3\":\"MAR\",\"country-code\":\"504\"},{\"name\":\"Mozambique\",\"alpha-3\":\"MOZ\",\"country-code\":\"508\"},{\"name\":\"Myanmar\",\"alpha-3\":\"MMR\",\"country-code\":\"104\"},{\"name\":\"Namibia\",\"alpha-3\":\"NAM\",\"country-code\":\"516\"},{\"name\":\"Nauru\",\"alpha-3\":\"NRU\",\"country-code\":\"520\"},{\"name\":\"Nepal\",\"alpha-3\":\"NPL\",\"country-code\":\"524\"},{\"name\":\"Netherlands\",\"alpha-3\":\"NLD\",\"country-code\":\"528\"},{\"name\":\"New Caledonia\",\"alpha-3\":\"NCL\",\"country-code\":\"540\"},{\"name\":\"New Zealand\",\"alpha-3\":\"NZL\",\"country-code\":\"554\"},{\"name\":\"Nicaragua\",\"alpha-3\":\"NIC\",\"country-code\":\"558\"},{\"name\":\"Niger\",\"alpha-3\":\"NER\",\"country-code\":\"562\"},{\"name\":\"Nigeria\",\"alpha-3\":\"NGA\",\"country-code\":\"566\"},{\"name\":\"Niue\",\"alpha-3\":\"NIU\",\"country-code\":\"570\"},{\"name\":\"Norfolk Island\",\"alpha-3\":\"NFK\",\"country-code\":\"574\"},{\"name\":\"Northern Mariana Islands\",\"alpha-3\":\"MNP\",\"country-code\":\"580\"},{\"name\":\"Norway\",\"alpha-3\":\"NOR\",\"country-code\":\"578\"},{\"name\":\"Oman\",\"alpha-3\":\"OMN\",\"country-code\":\"512\"},{\"name\":\"Pakistan\",\"alpha-3\":\"PAK\",\"country-code\":\"586\"},{\"name\":\"Palau\",\"alpha-3\":\"PLW\",\"country-code\":\"585\"},{\"name\":\"Palestine, State of\",\"alpha-3\":\"PSE\",\"country-code\":\"275\"},{\"name\":\"Panama\",\"alpha-3\":\"PAN\",\"country-code\":\"591\"},{\"name\":\"Papua New Guinea\",\"alpha-3\":\"PNG\",\"country-code\":\"598\"},{\"name\":\"Paraguay\",\"alpha-3\":\"PRY\",\"country-code\":\"600\"},{\"name\":\"Peru\",\"alpha-3\":\"PER\",\"country-code\":\"604\"},{\"name\":\"Philippines\",\"alpha-3\":\"PHL\",\"country-code\":\"608\"},{\"name\":\"Pitcairn\",\"alpha-3\":\"PCN\",\"country-code\":\"612\"},{\"name\":\"Poland\",\"alpha-3\":\"POL\",\"country-code\":\"616\"},{\"name\":\"Portugal\",\"alpha-3\":\"PRT\",\"country-code\":\"620\"},{\"name\":\"Puerto Rico\",\"alpha-3\":\"PRI\",\"country-code\":\"630\"},{\"name\":\"Qatar\",\"alpha-3\":\"QAT\",\"country-code\":\"634\"},{\"name\":\"Réunion\",\"alpha-3\":\"REU\",\"country-code\":\"638\"},{\"name\":\"Romania\",\"alpha-3\":\"ROU\",\"country-code\":\"642\"},{\"name\":\"Russian Federation\",\"alpha-3\":\"RUS\",\"country-code\":\"643\"},{\"name\":\"Rwanda\",\"alpha-3\":\"RWA\",\"country-code\":\"646\"},{\"name\":\"Saint Barthélemy\",\"alpha-3\":\"BLM\",\"country-code\":\"652\"},{\"name\":\"Saint Helena, Ascension and Tristan da Cunha\",\"alpha-3\":\"SHN\",\"country-code\":\"654\"},{\"name\":\"Saint Kitts and Nevis\",\"alpha-3\":\"KNA\",\"country-code\":\"659\"},{\"name\":\"Saint Lucia\",\"alpha-3\":\"LCA\",\"country-code\":\"662\"},{\"name\":\"Saint Martin (French part)\",\"alpha-3\":\"MAF\",\"country-code\":\"663\"},{\"name\":\"Saint Pierre and Miquelon\",\"alpha-3\":\"SPM\",\"country-code\":\"666\"},{\"name\":\"Saint Vincent and the Grenadines\",\"alpha-3\":\"VCT\",\"country-code\":\"670\"},{\"name\":\"Samoa\",\"alpha-3\":\"WSM\",\"country-code\":\"882\"},{\"name\":\"San Marino\",\"alpha-3\":\"SMR\",\"country-code\":\"674\"},{\"name\":\"Sao Tome and Principe\",\"alpha-3\":\"STP\",\"country-code\":\"678\"},{\"name\":\"Saudi Arabia\",\"alpha-3\":\"SAU\",\"country-code\":\"682\"},{\"name\":\"Senegal\",\"alpha-3\":\"SEN\",\"country-code\":\"686\"},{\"name\":\"Serbia\",\"alpha-3\":\"SRB\",\"country-code\":\"688\"},{\"name\":\"Seychelles\",\"alpha-3\":\"SYC\",\"country-code\":\"690\"},{\"name\":\"Sierra Leone\",\"alpha-3\":\"SLE\",\"country-code\":\"694\"},{\"name\":\"Singapore\",\"alpha-3\":\"SGP\",\"country-code\":\"702\"},{\"name\":\"Sint Maarten (Dutch part)\",\"alpha-3\":\"SXM\",\"country-code\":\"534\"},{\"name\":\"Slovakia\",\"alpha-3\":\"SVK\",\"country-code\":\"703\"},{\"name\":\"Slovenia\",\"alpha-3\":\"SVN\",\"country-code\":\"705\"},{\"name\":\"Solomon Islands\",\"alpha-3\":\"SLB\",\"country-code\":\"090\"},{\"name\":\"Somalia\",\"alpha-3\":\"SOM\",\"country-code\":\"706\"},{\"name\":\"South Africa\",\"alpha-3\":\"ZAF\",\"country-code\":\"710\"},{\"name\":\"South Georgia and the South Sandwich Islands\",\"alpha-3\":\"SGS\",\"country-code\":\"239\"},{\"name\":\"South Sudan\",\"alpha-3\":\"SSD\",\"country-code\":\"728\"},{\"name\":\"Spain\",\"alpha-3\":\"ESP\",\"country-code\":\"724\"},{\"name\":\"Sri Lanka\",\"alpha-3\":\"LKA\",\"country-code\":\"144\"},{\"name\":\"Sudan\",\"alpha-3\":\"SDN\",\"country-code\":\"729\"},{\"name\":\"Suriname\",\"alpha-3\":\"SUR\",\"country-code\":\"740\"},{\"name\":\"Svalbard and Jan Mayen\",\"alpha-3\":\"SJM\",\"country-code\":\"744\"},{\"name\":\"Swaziland\",\"alpha-3\":\"SWZ\",\"country-code\":\"748\"},{\"name\":\"Sweden\",\"alpha-3\":\"SWE\",\"country-code\":\"752\"},{\"name\":\"Switzerland\",\"alpha-3\":\"CHE\",\"country-code\":\"756\"},{\"name\":\"Syrian Arab Republic\",\"alpha-3\":\"SYR\",\"country-code\":\"760\"},{\"name\":\"Taiwan, Province of China\",\"alpha-3\":\"TWN\",\"country-code\":\"158\"},{\"name\":\"Tajikistan\",\"alpha-3\":\"TJK\",\"country-code\":\"762\"},{\"name\":\"Tanzania, United Republic of\",\"alpha-3\":\"TZA\",\"country-code\":\"834\"},{\"name\":\"Thailand\",\"alpha-3\":\"THA\",\"country-code\":\"764\"},{\"name\":\"Timor-Leste\",\"alpha-3\":\"TLS\",\"country-code\":\"626\"},{\"name\":\"Togo\",\"alpha-3\":\"TGO\",\"country-code\":\"768\"},{\"name\":\"Tokelau\",\"alpha-3\":\"TKL\",\"country-code\":\"772\"},{\"name\":\"Tonga\",\"alpha-3\":\"TON\",\"country-code\":\"776\"},{\"name\":\"Trinidad and Tobago\",\"alpha-3\":\"TTO\",\"country-code\":\"780\"},{\"name\":\"Tunisia\",\"alpha-3\":\"TUN\",\"country-code\":\"788\"},{\"name\":\"Turkey\",\"alpha-3\":\"TUR\",\"country-code\":\"792\"},{\"name\":\"Turkmenistan\",\"alpha-3\":\"TKM\",\"country-code\":\"795\"},{\"name\":\"Turks and Caicos Islands\",\"alpha-3\":\"TCA\",\"country-code\":\"796\"},{\"name\":\"Tuvalu\",\"alpha-3\":\"TUV\",\"country-code\":\"798\"},{\"name\":\"Uganda\",\"alpha-3\":\"UGA\",\"country-code\":\"800\"},{\"name\":\"Ukraine\",\"alpha-3\":\"UKR\",\"country-code\":\"804\"},{\"name\":\"United Arab Emirates\",\"alpha-3\":\"ARE\",\"country-code\":\"784\"},{\"name\":\"United Kingdom of Great Britain and Northern Ireland\",\"alpha-3\":\"GBR\",\"country-code\":\"826\"},{\"name\":\"United States of America\",\"alpha-3\":\"USA\",\"country-code\":\"840\"},{\"name\":\"United States Minor Outlying Islands\",\"alpha-3\":\"UMI\",\"country-code\":\"581\"},{\"name\":\"Uruguay\",\"alpha-3\":\"URY\",\"country-code\":\"858\"},{\"name\":\"Uzbekistan\",\"alpha-3\":\"UZB\",\"country-code\":\"860\"},{\"name\":\"Vanuatu\",\"alpha-3\":\"VUT\",\"country-code\":\"548\"},{\"name\":\"Venezuela (Bolivarian Republic of)\",\"alpha-3\":\"VEN\",\"country-code\":\"862\"},{\"name\":\"Viet Nam\",\"alpha-3\":\"VNM\",\"country-code\":\"704\"},{\"name\":\"Virgin Islands (British)\",\"alpha-3\":\"VGB\",\"country-code\":\"092\"},{\"name\":\"Virgin Islands (U.S.)\",\"alpha-3\":\"VIR\",\"country-code\":\"850\"},{\"name\":\"Wallis and Futuna\",\"alpha-3\":\"WLF\",\"country-code\":\"876\"},{\"name\":\"Western Sahara\",\"alpha-3\":\"ESH\",\"country-code\":\"732\"},{\"name\":\"Yemen\",\"alpha-3\":\"YEM\",\"country-code\":\"887\"},{\"name\":\"Zambia\",\"alpha-3\":\"ZMB\",\"country-code\":\"894\"},{\"name\":\"Zimbabwe\",\"alpha-3\":\"ZWE\",\"country-code\":\"716\"}]"
//    static def countryJsonString = "[{\"name\":\"\",\"\":\"\",\"country-code\":\"000\"},{\"name\":\"Afghanistan\",\"alpha-3\":\"AFG\",\"country-code\":\"004\"},{\"name\":\"Åland Islands\",\"alpha-3\":\"ALA\",\"country-code\":\"248\"},{\"name\":\"Albania\",\"alpha-3\":\"ALB\",\"country-code\":\"008\"},{\"name\":\"Algeria\",\"alpha-3\":\"DZA\",\"country-code\":\"012\"},{\"name\":\"American Samoa\",\"alpha-3\":\"ASM\",\"country-code\":\"016\"},{\"name\":\"Andorra\",\"alpha-3\":\"AND\",\"country-code\":\"020\"},{\"name\":\"Angola\",\"alpha-3\":\"AGO\",\"country-code\":\"024\"},{\"name\":\"Anguilla\",\"alpha-3\":\"AIA\",\"country-code\":\"660\"},{\"name\":\"Antarctica\",\"alpha-3\":\"ATA\",\"country-code\":\"010\"},{\"name\":\"Antigua and Barbuda\",\"alpha-3\":\"ATG\",\"country-code\":\"028\"},{\"name\":\"Argentina\",\"alpha-3\":\"ARG\",\"country-code\":\"032\"},{\"name\":\"Armenia\",\"alpha-3\":\"ARM\",\"country-code\":\"051\"},{\"name\":\"Aruba\",\"alpha-3\":\"ABW\",\"country-code\":\"533\"},{\"name\":\"Australia\",\"alpha-3\":\"AUS\",\"country-code\":\"036\"},{\"name\":\"Austria\",\"alpha-3\":\"AUT\",\"country-code\":\"040\"},{\"name\":\"Azerbaijan\",\"alpha-3\":\"AZE\",\"country-code\":\"031\"},{\"name\":\"Bahamas\",\"alpha-3\":\"BHS\",\"country-code\":\"044\"},{\"name\":\"Bahrain\",\"alpha-3\":\"BHR\",\"country-code\":\"048\"},{\"name\":\"Bangladesh\",\"alpha-3\":\"BGD\",\"country-code\":\"050\"},{\"name\":\"Barbados\",\"alpha-3\":\"BRB\",\"country-code\":\"052\"},{\"name\":\"Belarus\",\"alpha-3\":\"BLR\",\"country-code\":\"112\"},{\"name\":\"Belgium\",\"alpha-3\":\"BEL\",\"country-code\":\"056\"},{\"name\":\"Belize\",\"alpha-3\":\"BLZ\",\"country-code\":\"084\"},{\"name\":\"Benin\",\"alpha-3\":\"BEN\",\"country-code\":\"204\"},{\"name\":\"Bermuda\",\"alpha-3\":\"BMU\",\"country-code\":\"060\"},{\"name\":\"Bhutan\",\"alpha-3\":\"BTN\",\"country-code\":\"064\"},{\"name\":\"Bolivia (Plurinational State of)\",\"alpha-3\":\"BOL\",\"country-code\":\"068\"},{\"name\":\"Bonaire, Sint Eustatius and Saba\",\"alpha-3\":\"BES\",\"country-code\":\"535\"},{\"name\":\"Bosnia and Herzegovina\",\"alpha-3\":\"BIH\",\"country-code\":\"070\"},{\"name\":\"Botswana\",\"alpha-3\":\"BWA\",\"country-code\":\"072\"},{\"name\":\"Bouvet Island\",\"alpha-3\":\"BVT\",\"country-code\":\"074\"},{\"name\":\"Brazil\",\"alpha-3\":\"BRA\",\"country-code\":\"076\"},{\"name\":\"British Indian Ocean Territory\",\"alpha-3\":\"IOT\",\"country-code\":\"086\"},{\"name\":\"Brunei Darussalam\",\"alpha-3\":\"BRN\",\"country-code\":\"096\"},{\"name\":\"Bulgaria\",\"alpha-3\":\"BGR\",\"country-code\":\"100\"},{\"name\":\"Burkina Faso\",\"alpha-3\":\"BFA\",\"country-code\":\"854\"},{\"name\":\"Burundi\",\"alpha-3\":\"BDI\",\"country-code\":\"108\"},{\"name\":\"Cambodia\",\"alpha-3\":\"KHM\",\"country-code\":\"116\"},{\"name\":\"Cameroon\",\"alpha-3\":\"CMR\",\"country-code\":\"120\"},{\"name\":\"Canada\",\"alpha-3\":\"CAN\",\"country-code\":\"124\"},{\"name\":\"Cabo Verde\",\"alpha-3\":\"CPV\",\"country-code\":\"132\"},{\"name\":\"Cayman Islands\",\"alpha-3\":\"CYM\",\"country-code\":\"136\"},{\"name\":\"Central African Republic\",\"alpha-3\":\"CAF\",\"country-code\":\"140\"},{\"name\":\"Chad\",\"alpha-3\":\"TCD\",\"country-code\":\"148\"},{\"name\":\"Chile\",\"alpha-3\":\"CHL\",\"country-code\":\"152\"},{\"name\":\"China\",\"alpha-3\":\"CHN\",\"country-code\":\"156\"},{\"name\":\"Christmas Island\",\"alpha-3\":\"CXR\",\"country-code\":\"162\"},{\"name\":\"Cocos (Keeling) Islands\",\"alpha-3\":\"CCK\",\"country-code\":\"166\"},{\"name\":\"Colombia\",\"alpha-3\":\"COL\",\"country-code\":\"170\"},{\"name\":\"Comoros\",\"alpha-3\":\"COM\",\"country-code\":\"174\"},{\"name\":\"Congo\",\"alpha-3\":\"COG\",\"country-code\":\"178\"},{\"name\":\"Congo (Democratic Republic of the)\",\"alpha-3\":\"COD\",\"country-code\":\"180\"},{\"name\":\"Cook Islands\",\"alpha-3\":\"COK\",\"country-code\":\"184\"},{\"name\":\"Costa Rica\",\"alpha-3\":\"CRI\",\"country-code\":\"188\"},{\"name\":\"Côte d'Ivoire\",\"alpha-3\":\"CIV\",\"country-code\":\"384\"},{\"name\":\"Croatia\",\"alpha-3\":\"HRV\",\"country-code\":\"191\"},{\"name\":\"Cuba\",\"alpha-3\":\"CUB\",\"country-code\":\"192\"},{\"name\":\"Curaçao\",\"alpha-3\":\"CUW\",\"country-code\":\"531\"},{\"name\":\"Cyprus\",\"alpha-3\":\"CYP\",\"country-code\":\"196\"},{\"name\":\"Czech Republic\",\"alpha-3\":\"CZE\",\"country-code\":\"203\"},{\"name\":\"Denmark\",\"alpha-3\":\"DNK\",\"country-code\":\"208\"},{\"name\":\"Djibouti\",\"alpha-3\":\"DJI\",\"country-code\":\"262\"},{\"name\":\"Dominica\",\"alpha-3\":\"DMA\",\"country-code\":\"212\"},{\"name\":\"Dominican Republic\",\"alpha-3\":\"DOM\",\"country-code\":\"214\"},{\"name\":\"Ecuador\",\"alpha-3\":\"ECU\",\"country-code\":\"218\"},{\"name\":\"Egypt\",\"alpha-3\":\"EGY\",\"country-code\":\"818\"},{\"name\":\"El Salvador\",\"alpha-3\":\"SLV\",\"country-code\":\"222\"},{\"name\":\"Equatorial Guinea\",\"alpha-3\":\"GNQ\",\"country-code\":\"226\"},{\"name\":\"Eritrea\",\"alpha-3\":\"ERI\",\"country-code\":\"232\"},{\"name\":\"Estonia\",\"alpha-3\":\"EST\",\"country-code\":\"233\"},{\"name\":\"Ethiopia\",\"alpha-3\":\"ETH\",\"country-code\":\"231\"},{\"name\":\"Falkland Islands (Malvinas)\",\"alpha-3\":\"FLK\",\"country-code\":\"238\"},{\"name\":\"Faroe Islands\",\"alpha-3\":\"FRO\",\"country-code\":\"234\"},{\"name\":\"Fiji\",\"alpha-3\":\"FJI\",\"country-code\":\"242\"},{\"name\":\"Finland\",\"alpha-3\":\"FIN\",\"country-code\":\"246\"},{\"name\":\"France\",\"alpha-3\":\"FRA\",\"country-code\":\"250\"},{\"name\":\"French Guiana\",\"alpha-3\":\"GUF\",\"country-code\":\"254\"},{\"name\":\"French Polynesia\",\"alpha-3\":\"PYF\",\"country-code\":\"258\"},{\"name\":\"French Southern Territories\",\"alpha-3\":\"ATF\",\"country-code\":\"260\"},{\"name\":\"Gabon\",\"alpha-3\":\"GAB\",\"country-code\":\"266\"},{\"name\":\"Gambia\",\"alpha-3\":\"GMB\",\"country-code\":\"270\"},{\"name\":\"Georgia\",\"alpha-3\":\"GEO\",\"country-code\":\"268\"},{\"name\":\"Germany\",\"alpha-3\":\"DEU\",\"country-code\":\"276\"},{\"name\":\"Ghana\",\"alpha-3\":\"GHA\",\"country-code\":\"288\"},{\"name\":\"Gibraltar\",\"alpha-3\":\"GIB\",\"country-code\":\"292\"},{\"name\":\"Greece\",\"alpha-3\":\"GRC\",\"country-code\":\"300\"},{\"name\":\"Greenland\",\"alpha-3\":\"GRL\",\"country-code\":\"304\"},{\"name\":\"Grenada\",\"alpha-3\":\"GRD\",\"country-code\":\"308\"},{\"name\":\"Guadeloupe\",\"alpha-3\":\"GLP\",\"country-code\":\"312\"},{\"name\":\"Guam\",\"alpha-3\":\"GUM\",\"country-code\":\"316\"},{\"name\":\"Guatemala\",\"alpha-3\":\"GTM\",\"country-code\":\"320\"},{\"name\":\"Guernsey\",\"alpha-3\":\"GGY\",\"country-code\":\"831\"},{\"name\":\"Guinea\",\"alpha-3\":\"GIN\",\"country-code\":\"324\"},{\"name\":\"Guinea-Bissau\",\"alpha-3\":\"GNB\",\"country-code\":\"624\"},{\"name\":\"Guyana\",\"alpha-3\":\"GUY\",\"country-code\":\"328\"},{\"name\":\"Haiti\",\"alpha-3\":\"HTI\",\"country-code\":\"332\"},{\"name\":\"Heard Island and McDonald Islands\",\"alpha-3\":\"HMD\",\"country-code\":\"334\"},{\"name\":\"Holy See\",\"alpha-3\":\"VAT\",\"country-code\":\"336\"},{\"name\":\"Honduras\",\"alpha-3\":\"HND\",\"country-code\":\"340\"},{\"name\":\"Hong Kong\",\"alpha-3\":\"HKG\",\"country-code\":\"344\"},{\"name\":\"Hungary\",\"alpha-3\":\"HUN\",\"country-code\":\"348\"},{\"name\":\"Iceland\",\"alpha-3\":\"ISL\",\"country-code\":\"352\"},{\"name\":\"India\",\"alpha-3\":\"IND\",\"country-code\":\"356\"},{\"name\":\"Indonesia\",\"alpha-3\":\"IDN\",\"country-code\":\"360\"},{\"name\":\"Iran (Islamic Republic of)\",\"alpha-3\":\"IRN\",\"country-code\":\"364\"},{\"name\":\"Iraq\",\"alpha-3\":\"IRQ\",\"country-code\":\"368\"},{\"name\":\"Ireland\",\"alpha-3\":\"IRL\",\"country-code\":\"372\"},{\"name\":\"Isle of Man\",\"alpha-3\":\"IMN\",\"country-code\":\"833\"},{\"name\":\"Israel\",\"alpha-3\":\"ISR\",\"country-code\":\"376\"},{\"name\":\"Italy\",\"alpha-3\":\"ITA\",\"country-code\":\"380\"},{\"name\":\"Jamaica\",\"alpha-3\":\"JAM\",\"country-code\":\"388\"},{\"name\":\"Japan\",\"alpha-3\":\"JPN\",\"country-code\":\"392\"},{\"name\":\"Jersey\",\"alpha-3\":\"JEY\",\"country-code\":\"832\"},{\"name\":\"Jordan\",\"alpha-3\":\"JOR\",\"country-code\":\"400\"},{\"name\":\"Kazakhstan\",\"alpha-3\":\"KAZ\",\"country-code\":\"398\"},{\"name\":\"Kenya\",\"alpha-3\":\"KEN\",\"country-code\":\"404\"},{\"name\":\"Kiribati\",\"alpha-3\":\"KIR\",\"country-code\":\"296\"},{\"name\":\"Korea (Democratic People's Republic of)\",\"alpha-3\":\"PRK\",\"country-code\":\"408\"},{\"name\":\"Korea (Republic of)\",\"alpha-3\":\"KOR\",\"country-code\":\"410\"},{\"name\":\"Kuwait\",\"alpha-3\":\"KWT\",\"country-code\":\"414\"},{\"name\":\"Kyrgyzstan\",\"alpha-3\":\"KGZ\",\"country-code\":\"417\"},{\"name\":\"Lao People's Democratic Republic\",\"alpha-3\":\"LAO\",\"country-code\":\"418\"},{\"name\":\"Latvia\",\"alpha-3\":\"LVA\",\"country-code\":\"428\"},{\"name\":\"Lebanon\",\"alpha-3\":\"LBN\",\"country-code\":\"422\"},{\"name\":\"Lesotho\",\"alpha-3\":\"LSO\",\"country-code\":\"426\"},{\"name\":\"Liberia\",\"alpha-3\":\"LBR\",\"country-code\":\"430\"},{\"name\":\"Libya\",\"alpha-3\":\"LBY\",\"country-code\":\"434\"},{\"name\":\"Liechtenstein\",\"alpha-3\":\"LIE\",\"country-code\":\"438\"},{\"name\":\"Lithuania\",\"alpha-3\":\"LTU\",\"country-code\":\"440\"},{\"name\":\"Luxembourg\",\"alpha-3\":\"LUX\",\"country-code\":\"442\"},{\"name\":\"Macao\",\"alpha-3\":\"MAC\",\"country-code\":\"446\"},{\"name\":\"Macedonia (the former Yugoslav Republic of)\",\"alpha-3\":\"MKD\",\"country-code\":\"807\"},{\"name\":\"Madagascar\",\"alpha-3\":\"MDG\",\"country-code\":\"450\"},{\"name\":\"Malawi\",\"alpha-3\":\"MWI\",\"country-code\":\"454\"},{\"name\":\"Malaysia\",\"alpha-3\":\"MYS\",\"country-code\":\"458\"},{\"name\":\"Maldives\",\"alpha-3\":\"MDV\",\"country-code\":\"462\"},{\"name\":\"Mali\",\"alpha-3\":\"MLI\",\"country-code\":\"466\"},{\"name\":\"Malta\",\"alpha-3\":\"MLT\",\"country-code\":\"470\"},{\"name\":\"Marshall Islands\",\"alpha-3\":\"MHL\",\"country-code\":\"584\"},{\"name\":\"Martinique\",\"alpha-3\":\"MTQ\",\"country-code\":\"474\"},{\"name\":\"Mauritania\",\"alpha-3\":\"MRT\",\"country-code\":\"478\"},{\"name\":\"Mauritius\",\"alpha-3\":\"MUS\",\"country-code\":\"480\"},{\"name\":\"Mayotte\",\"alpha-3\":\"MYT\",\"country-code\":\"175\"},{\"name\":\"Mexico\",\"alpha-3\":\"MEX\",\"country-code\":\"484\"},{\"name\":\"Micronesia (Federated States of)\",\"alpha-3\":\"FSM\",\"country-code\":\"583\"},{\"name\":\"Moldova (Republic of)\",\"alpha-3\":\"MDA\",\"country-code\":\"498\"},{\"name\":\"Monaco\",\"alpha-3\":\"MCO\",\"country-code\":\"492\"},{\"name\":\"Mongolia\",\"alpha-3\":\"MNG\",\"country-code\":\"496\"},{\"name\":\"Montenegro\",\"alpha-3\":\"MNE\",\"country-code\":\"499\"},{\"name\":\"Montserrat\",\"alpha-3\":\"MSR\",\"country-code\":\"500\"},{\"name\":\"Morocco\",\"alpha-3\":\"MAR\",\"country-code\":\"504\"},{\"name\":\"Mozambique\",\"alpha-3\":\"MOZ\",\"country-code\":\"508\"},{\"name\":\"Myanmar\",\"alpha-3\":\"MMR\",\"country-code\":\"104\"},{\"name\":\"Namibia\",\"alpha-3\":\"NAM\",\"country-code\":\"516\"},{\"name\":\"Nauru\",\"alpha-3\":\"NRU\",\"country-code\":\"520\"},{\"name\":\"Nepal\",\"alpha-3\":\"NPL\",\"country-code\":\"524\"},{\"name\":\"Netherlands\",\"alpha-3\":\"NLD\",\"country-code\":\"528\"},{\"name\":\"New Caledonia\",\"alpha-3\":\"NCL\",\"country-code\":\"540\"},{\"name\":\"New Zealand\",\"alpha-3\":\"NZL\",\"country-code\":\"554\"},{\"name\":\"Nicaragua\",\"alpha-3\":\"NIC\",\"country-code\":\"558\"},{\"name\":\"Niger\",\"alpha-3\":\"NER\",\"country-code\":\"562\"},{\"name\":\"Nigeria\",\"alpha-3\":\"NGA\",\"country-code\":\"566\"},{\"name\":\"Niue\",\"alpha-3\":\"NIU\",\"country-code\":\"570\"},{\"name\":\"Norfolk Island\",\"alpha-3\":\"NFK\",\"country-code\":\"574\"},{\"name\":\"Northern Mariana Islands\",\"alpha-3\":\"MNP\",\"country-code\":\"580\"},{\"name\":\"Norway\",\"alpha-3\":\"NOR\",\"country-code\":\"578\"},{\"name\":\"Oman\",\"alpha-3\":\"OMN\",\"country-code\":\"512\"},{\"name\":\"Pakistan\",\"alpha-3\":\"PAK\",\"country-code\":\"586\"},{\"name\":\"Palau\",\"alpha-3\":\"PLW\",\"country-code\":\"585\"},{\"name\":\"Palestine, State of\",\"alpha-3\":\"PSE\",\"country-code\":\"275\"},{\"name\":\"Panama\",\"alpha-3\":\"PAN\",\"country-code\":\"591\"},{\"name\":\"Papua New Guinea\",\"alpha-3\":\"PNG\",\"country-code\":\"598\"},{\"name\":\"Paraguay\",\"alpha-3\":\"PRY\",\"country-code\":\"600\"},{\"name\":\"Peru\",\"alpha-3\":\"PER\",\"country-code\":\"604\"},{\"name\":\"Philippines\",\"alpha-3\":\"PHL\",\"country-code\":\"608\"},{\"name\":\"Pitcairn\",\"alpha-3\":\"PCN\",\"country-code\":\"612\"},{\"name\":\"Poland\",\"alpha-3\":\"POL\",\"country-code\":\"616\"},{\"name\":\"Portugal\",\"alpha-3\":\"PRT\",\"country-code\":\"620\"},{\"name\":\"Puerto Rico\",\"alpha-3\":\"PRI\",\"country-code\":\"630\"},{\"name\":\"Qatar\",\"alpha-3\":\"QAT\",\"country-code\":\"634\"},{\"name\":\"Réunion\",\"alpha-3\":\"REU\",\"country-code\":\"638\"},{\"name\":\"Romania\",\"alpha-3\":\"ROU\",\"country-code\":\"642\"},{\"name\":\"Russian Federation\",\"alpha-3\":\"RUS\",\"country-code\":\"643\"},{\"name\":\"Rwanda\",\"alpha-3\":\"RWA\",\"country-code\":\"646\"},{\"name\":\"Saint Barthélemy\",\"alpha-3\":\"BLM\",\"country-code\":\"652\"},{\"name\":\"Saint Helena, Ascension and Tristan da Cunha\",\"alpha-3\":\"SHN\",\"country-code\":\"654\"},{\"name\":\"Saint Kitts and Nevis\",\"alpha-3\":\"KNA\",\"country-code\":\"659\"},{\"name\":\"Saint Lucia\",\"alpha-3\":\"LCA\",\"country-code\":\"662\"},{\"name\":\"Saint Martin (French part)\",\"alpha-3\":\"MAF\",\"country-code\":\"663\"},{\"name\":\"Saint Pierre and Miquelon\",\"alpha-3\":\"SPM\",\"country-code\":\"666\"},{\"name\":\"Saint Vincent and the Grenadines\",\"alpha-3\":\"VCT\",\"country-code\":\"670\"},{\"name\":\"Samoa\",\"alpha-3\":\"WSM\",\"country-code\":\"882\"},{\"name\":\"San Marino\",\"alpha-3\":\"SMR\",\"country-code\":\"674\"},{\"name\":\"Sao Tome and Principe\",\"alpha-3\":\"STP\",\"country-code\":\"678\"},{\"name\":\"Saudi Arabia\",\"alpha-3\":\"SAU\",\"country-code\":\"682\"},{\"name\":\"Senegal\",\"alpha-3\":\"SEN\",\"country-code\":\"686\"},{\"name\":\"Serbia\",\"alpha-3\":\"SRB\",\"country-code\":\"688\"},{\"name\":\"Seychelles\",\"alpha-3\":\"SYC\",\"country-code\":\"690\"},{\"name\":\"Sierra Leone\",\"alpha-3\":\"SLE\",\"country-code\":\"694\"},{\"name\":\"Singapore\",\"alpha-3\":\"SGP\",\"country-code\":\"702\"},{\"name\":\"Sint Maarten (Dutch part)\",\"alpha-3\":\"SXM\",\"country-code\":\"534\"},{\"name\":\"Slovakia\",\"alpha-3\":\"SVK\",\"country-code\":\"703\"},{\"name\":\"Slovenia\",\"alpha-3\":\"SVN\",\"country-code\":\"705\"},{\"name\":\"Solomon Islands\",\"alpha-3\":\"SLB\",\"country-code\":\"090\"},{\"name\":\"Somalia\",\"alpha-3\":\"SOM\",\"country-code\":\"706\"},{\"name\":\"South Africa\",\"alpha-3\":\"ZAF\",\"country-code\":\"710\"},{\"name\":\"South Georgia and the South Sandwich Islands\",\"alpha-3\":\"SGS\",\"country-code\":\"239\"},{\"name\":\"South Sudan\",\"alpha-3\":\"SSD\",\"country-code\":\"728\"},{\"name\":\"Spain\",\"alpha-3\":\"ESP\",\"country-code\":\"724\"},{\"name\":\"Sri Lanka\",\"alpha-3\":\"LKA\",\"country-code\":\"144\"},{\"name\":\"Sudan\",\"alpha-3\":\"SDN\",\"country-code\":\"729\"},{\"name\":\"Suriname\",\"alpha-3\":\"SUR\",\"country-code\":\"740\"},{\"name\":\"Svalbard and Jan Mayen\",\"alpha-3\":\"SJM\",\"country-code\":\"744\"},{\"name\":\"Swaziland\",\"alpha-3\":\"SWZ\",\"country-code\":\"748\"},{\"name\":\"Sweden\",\"alpha-3\":\"SWE\",\"country-code\":\"752\"},{\"name\":\"Switzerland\",\"alpha-3\":\"CHE\",\"country-code\":\"756\"},{\"name\":\"Syrian Arab Republic\",\"alpha-3\":\"SYR\",\"country-code\":\"760\"},{\"name\":\"Taiwan, Province of China\",\"alpha-3\":\"TWN\",\"country-code\":\"158\"},{\"name\":\"Tajikistan\",\"alpha-3\":\"TJK\",\"country-code\":\"762\"},{\"name\":\"Tanzania, United Republic of\",\"alpha-3\":\"TZA\",\"country-code\":\"834\"},{\"name\":\"Thailand\",\"alpha-3\":\"THA\",\"country-code\":\"764\"},{\"name\":\"Timor-Leste\",\"alpha-3\":\"TLS\",\"country-code\":\"626\"},{\"name\":\"Togo\",\"alpha-3\":\"TGO\",\"country-code\":\"768\"},{\"name\":\"Tokelau\",\"alpha-3\":\"TKL\",\"country-code\":\"772\"},{\"name\":\"Tonga\",\"alpha-3\":\"TON\",\"country-code\":\"776\"},{\"name\":\"Trinidad and Tobago\",\"alpha-3\":\"TTO\",\"country-code\":\"780\"},{\"name\":\"Tunisia\",\"alpha-3\":\"TUN\",\"country-code\":\"788\"},{\"name\":\"Turkey\",\"alpha-3\":\"TUR\",\"country-code\":\"792\"},{\"name\":\"Turkmenistan\",\"alpha-3\":\"TKM\",\"country-code\":\"795\"},{\"name\":\"Turks and Caicos Islands\",\"alpha-3\":\"TCA\",\"country-code\":\"796\"},{\"name\":\"Tuvalu\",\"alpha-3\":\"TUV\",\"country-code\":\"798\"},{\"name\":\"Uganda\",\"alpha-3\":\"UGA\",\"country-code\":\"800\"},{\"name\":\"Ukraine\",\"alpha-3\":\"UKR\",\"country-code\":\"804\"},{\"name\":\"United Arab Emirates\",\"alpha-3\":\"ARE\",\"country-code\":\"784\"},{\"name\":\"United Kingdom of Great Britain and Northern Ireland\",\"alpha-3\":\"GBR\",\"country-code\":\"826\"},{\"name\":\"United States of America\",\"alpha-3\":\"USA\",\"country-code\":\"840\"},{\"name\":\"United States Minor Outlying Islands\",\"alpha-3\":\"UMI\",\"country-code\":\"581\"},{\"name\":\"Uruguay\",\"alpha-3\":\"URY\",\"country-code\":\"858\"},{\"name\":\"Uzbekistan\",\"alpha-3\":\"UZB\",\"country-code\":\"860\"},{\"name\":\"Vanuatu\",\"alpha-3\":\"VUT\",\"country-code\":\"548\"},{\"name\":\"Venezuela (Bolivarian Republic of)\",\"alpha-3\":\"VEN\",\"country-code\":\"862\"},{\"name\":\"Viet Nam\",\"alpha-3\":\"VNM\",\"country-code\":\"704\"},{\"name\":\"Virgin Islands (British)\",\"alpha-3\":\"VGB\",\"country-code\":\"092\"},{\"name\":\"Virgin Islands (U.S.)\",\"alpha-3\":\"VIR\",\"country-code\":\"850\"},{\"name\":\"Wallis and Futuna\",\"alpha-3\":\"WLF\",\"country-code\":\"876\"},{\"name\":\"Western Sahara\",\"alpha-3\":\"ESH\",\"country-code\":\"732\"},{\"name\":\"Yemen\",\"alpha-3\":\"YEM\",\"country-code\":\"887\"},{\"name\":\"Zambia\",\"alpha-3\":\"ZMB\",\"country-code\":\"894\"},{\"name\":\"Zimbabwe\",\"alpha-3\":\"ZWE\",\"country-code\":\"716\"}]"

    static JSONArray countryJson = (JSONArray) JSON.parse(countryJsonString)

    def country () {
        def queryJSON = request.JSON
        SuggestQuery vq = new SuggestQuery(queryJSON)
        String query = vq.getQuery()
        List<CountrySuggestion> suggestions = new ArrayList<CountrySuggestion>()
        if (query) {
//            CountrySuggestion blank = new CountrySuggestion()
//            blank.setSuggestion("clear")
//            suggestions.add(blank)
            for ( int i = 0; i < countryJson.size(); i++  ) {
                JSONObject cntry = countryJson.get(i)
                if ( cntry.getString("name").toLowerCase().contains(query) ) {
                    CountrySuggestion v = new CountrySuggestion()
                    v.setSuggestion(cntry.getString("name"))
                    suggestions.add(v)
                }
            }
        }
        render suggestions as JSON
    }

    static String getThreeLetter(String countryName) {
        if ( !countryName ) { return null }
        countryName = countryName.trim()
        for ( int i = 0; i < countryJson.size(); i++  ) {
            JSONObject cntry = countryJson.get(i)
            if (cntry.getString("name").toLowerCase().equals(countryName.toLowerCase())) {
                return cntry.getString("alpha-3")
            }
        }
    }
    static String getCountryName(String threeLetter) {
        if ( !threeLetter ) { return null }
        threeLetter = threeLetter.trim()
        for ( int i = 0; i < countryJson.size(); i++  ) {
            JSONObject cntry = countryJson.get(i)
            if (cntry.getString("alpha-3").toLowerCase().equals(threeLetter.toLowerCase())) {
                return cntry.getString("name")
            }
        }
    }
}
