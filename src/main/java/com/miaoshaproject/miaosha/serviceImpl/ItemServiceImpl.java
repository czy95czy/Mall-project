package com.miaoshaproject.miaosha.serviceImpl;

import com.miaoshaproject.miaosha.dao.ItemDOMapper;
import com.miaoshaproject.miaosha.dao.ItemStockDOMapper;
import com.miaoshaproject.miaosha.dataObject.ItemDO;
import com.miaoshaproject.miaosha.dataObject.ItemStockDO;
import com.miaoshaproject.miaosha.error.BusinessException;
import com.miaoshaproject.miaosha.error.EmBusinessError;
import com.miaoshaproject.miaosha.service.ItemService;
import com.miaoshaproject.miaosha.service.model.ItemModel;
import com.miaoshaproject.miaosha.validator.ValidationResult;
import com.miaoshaproject.miaosha.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author cuizhiyuan
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;
    @Resource
    private ItemDOMapper itemDOMapper;

    @Resource
    private ItemStockDOMapper itemStockDOMapper;

    private ItemDO convertItemDOFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        //将price的BigDecimal类型转为Double类型，因为copy时不会copy不一样的类型
        itemDO.setPrice(itemModel.getPrice().doubleValue());

        return itemDO;
    }
    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel) throws BusinessException {
         if (itemModel == null){
             return null;
         }
         ItemStockDO itemStockDO = new ItemStockDO();
         itemStockDO.setItemId(itemModel.getId());
         itemStockDO.setStock(itemModel.getStock());
         return itemStockDO;
    }


    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        ValidationResult result = validator.validate(itemModel);
        //校验入参
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.gerErrorMsg());
        }
        //转化itemModel——————>dataObject
         ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);


        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        itemDOMapper.selectByPrimaryKey(itemDO.getId());

        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        return null;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null ){
            return null;
        }
        //获得库存数量
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemById(itemDO.getId());


        //将dataObject---->moodel

        ItemModel itemModel = convertModelFromDataObject(itemDO,itemStockDO);
        return itemModel;
    }
        private ItemModel convertModelFromDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
            ItemModel itemModel = new ItemModel();
            BeanUtils.copyProperties(itemDO,itemModel);
            itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
            itemModel.setStock(itemStockDO.getStock());
            return itemModel;
        }


}
