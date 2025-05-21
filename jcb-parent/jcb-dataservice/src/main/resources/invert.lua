local productId = ARGV[1]
local userId = ARGV[2]
local money = tonumber(ARGV[3])

local stockKey = 'invert:stock:' .. productId
local userBidKey = 'invert:bid:' .. productId

-- 获取并转换数值，提供默认值或返回错误
local min = tonumber(redis.call('HGET', stockKey, 'min'))
local max = tonumber(redis.call('HGET', stockKey, 'max'))
local left = tonumber(redis.call('HGET', stockKey, 'left'))

-- 检查必要的值是否存在
if not min or not max or not left then
    return -1 -- 表示产品信息不完整
end

-- 参数校验
if money > left or money < min or money > max then
    return 0
end

-- 获取用户当前投标次数
local count = redis.call('ZSCORE', userBidKey, userId)
count = count and tonumber(count) or 0

-- 检查投标次数限制
if count >= 10 then
    return 1
end

-- 更新投标次数
local newScore = count + 1
redis.call('ZADD', userBidKey, newScore, userId)

-- 更新剩余金额
local newLeft = left - money
redis.call('HSET', stockKey, 'left', newLeft)

-- 检查是否完成投标
if newLeft > 0 then
    return 2
else
    return 3
end