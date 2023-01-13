@echo ====ZQDownloader Building Process====
@echo Building webpack...
@cd front
@call npm install
@call npm run build
@echo Build complete.
@cd ..
@rd /s /q "back\src\main\resources\static\"
@cd "back\src\main\resources\"
@mkdir static
@echo Copying webpack file to static dir...
@cd ../../../..
@xcopy "front\dist\index.html" "back\src\main\resources\static\html\"
@xcopy "front\dist\css\*" "back\src\main\resources\static\down\css\"
@xcopy "front\dist\js\*" "back\src\main\resources\static\down\js\"
@cd back
@echo Compiling maven package...
@call mvn package
@echo Finished.
