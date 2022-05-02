SRC_DIR=program


.PHONY: all clean


all:
	@make -C $(SRC_DIR) all
	
clean:
	@make -C $(SRC_DIR) clean


