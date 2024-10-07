#!/bin/bash

if [ -f submission.zip ]; then
  old=$(date +%s)
  echo "Back up old submission as submission-${old}\n"
  mv submission.zip submission-${old}.zip
fi

zip -r submission.zip \
    src/farm/core \
    src/farm/files \
    src/farm/debugged \
    test/farm/inventory/BasicInventoryTest.java \
    test/farm/inventory/FancyInventoryTest.java \
    ai \
    -x \*package-info\* \
    src/farm/core/CustomerNotFoundException.java \
    src/farm/core/DuplicateCustomerException.java \
    src/farm/core/FailedTransactionException.java \
    src/farm/core/Farm.java \
    src/farm/core/InvalidStockRequestException.java \
    src/farm/core/ShopFront.java \
    src/farm/core/UnableToInteractException.java \
    src/farm/core/farmgrid/Grid.java \
    \*.DS_Store\* \
    \*.class\*